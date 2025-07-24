# 0. Introduction

This document explores the testing capabilities provided by Spring Boot.
We'll delve into various strategies for effectively testing different layers of your application, aiming to build robust and reliable software.

# 1. End-to-End Testing

<br>

Before diving into specific layer testing, it's important to understand how to test the entire application flow.
`@SpringBootTest` with `webEnvironment = RANDOM_PORT` starts the full application context and allows testing the complete request-response cycle.

<br>

## 1.1. @SpringBootTest with TestRestTemplate

<br>

`@SpringBootTest` loads the complete application context, making it suitable for integration and end-to-end tests.
`TestRestTemplate` provides a convenient way to make HTTP requests to your running application.

<br>

```kotlin
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class E2ETest {
  @LocalServerPort
  private var port: Int = 0

  @Autowired
  lateinit var restTemplate: TestRestTemplate

  @Test
  fun testCreateAndGetMember() {
    // Create member via POST
    val createRequest = """{
            "name":"John",
            "email":"john@example.com"
        }"""
    val response = restTemplate.postForEntity(
      "http://localhost:$port/members",
      createRequest,
      Member::class.java
    )

    assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
  }
}
```

<br>

### Learning Test
- Test Method: `cholog.E2ETest.testCreateAndGetMember`
- Task: Write an end-to-end test that creates a member and then retrieves it.

<br>

### Learning Test
- Test Method: `cholog.E2ETest.testGetNonExistentMember`
- Task: Write a test for error handling when requesting a non-existent member.

<br>

### Reference
- [Spring Boot - Testing with TestRestTemplate](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.with-running-server)

<br>

# 2. Web Layer Testing

<br>

Spring provides various testing annotations to test different layers of your application.
`@WebMvcTest` is used to test the web layer by loading only the web-related components.

<br>

## 2.1. @WebMvcTest

<br>

`@WebMvcTest` loads only the web layer components, making controller tests fast and focused.
It automatically configures `MockMvc` and allows you to mock service dependencies with `@MockBean`.

<br>

```kotlin
@WebMvcTest(MemberController::class)
class MemberControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc
    
    @MockBean
    lateinit var memberService: MemberService
    
    @Test
    fun getMemberById() {
        // Given
        given(memberService.findById(1L)).willReturn(Member(1L, "John", "john@example.com"))
        
        // When & Then
        mockMvc.perform(get("/members/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John"))
    }
}
```

<br>

### Learning Test
- Test Method: `cholog.WebLayerTest.testGetMember`
- Task: Write a test for `MemberController.getMember` using `@WebMvcTest` and `MockMvc`.

<br>

## 2.2. POST Request Testing

<br>

Testing POST requests with JSON body requires setting the content type and providing request body.

<br>

```kotlin
@Test
fun createMember() {
    // Given
    given(memberService.create(any(), any())).willReturn(Member(1L, "Jane", "jane@example.com"))
    
    // When & Then
    mockMvc.perform(post("/members")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""{"name":"Jane","email":"jane@example.com"}"""))
        .andExpect(status().isCreated)
        .andExpect(header().string("Location", "/members/1"))
}
```

<br>

### Learning Test
- Test Method: `cholog.WebLayerTest.testCreateMember`
- Task: Write a test for `MemberController.createMember` with JSON body and Location header verification.

<br>

### Reference
- [Spring - Testing Web Layer](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.spring-mvc-tests)
- [Spring - MockMvc vs End-to-End Tests](https://docs.spring.io/spring-framework/reference/testing/mockmvc/vs-end-to-end-integration-tests.html)

<br>

# 3. Data Layer Testing

<br>

`@JdbcTest` provides a convenient way to test JDBC-based repositories by loading only JDBC-related components.

<br>

## 3.1. @JdbcTest

<br>

`@JdbcTest` configures an in-memory database and provides `JdbcTemplate` for testing data access logic.

<br>

```kotlin
@JdbcTest
class MemberRepositoryTest {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    
    @Autowired
    lateinit var memberRepository: MemberRepository
    
    @Test
    fun findById() {
        // Given
        jdbcTemplate.update("INSERT INTO members (id, name, email) VALUES (1, 'John', 'john@example.com')")
        
        // When
        val member = memberRepository.findById(1L)
        
        // Then
        assertThat(member?.name).isEqualTo("John")
    }
}
```

<br>

### Learning Test
- Test Method: `cholog.DataLayerTest.testFindById`
- Task: Write a test for `MemberRepository.findById` using `@JdbcTest` with test data preparation.

<br>

## 3.2. @Sql Annotation

<br>

`@Sql` annotation allows you to execute SQL scripts before test methods.

<br>

```kotlin
@Test
@Sql("/test-data.sql")
fun findAllMembers() {
    val members = memberRepository.findAll()
    assertThat(members).hasSize(3)
}
```

<br>

### Learning Test
- Test Method: `cholog.DataLayerTest.testFindAllWithSql`
- Task: Write a test for `MemberRepository.findAll` using `@Sql` annotation.

<br>

### Reference
- [Spring - Testing Data Layer](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.autoconfigured-jdbc-tests)

<br>

# 4. Service Layer Testing Strategies

<br>

There are different approaches to test service layer: using mocks, real repositories, or test configurations.
Each approach has its own benefits and trade-offs.

<br>

## 4.1. Testing with @Mock

<br>

You can use `@ExtendWith(SpringExtension::class)` with `@MockBean` for a lighter approach without full Spring Boot context.

<br>

```kotlin
@ExtendWith(SpringExtension::class)
class UserServiceMockTest {
    @Mock
    lateinit var userRepository: UserRepository
    
    @InjectMocks
    lateinit var userService: UserService
    
    @Test
    fun createMember() {
        // Given
        given(memberRepository.save(any())).willReturn(Member(1L, "John", "john@example.com"))
        
        // When
        val result = memberService.create("John", "john@example.com")
        
        // Then
        assertThat(result.id).isEqualTo(1L)
        verify(memberRepository).save(any())
    }
}
```

<br>

**Benefits**: Fast execution, no external dependencies, easy to test specific scenarios, lighter than @SpringBootTest
**Drawbacks**: May not catch integration issues, requires maintaining mock behavior

<br>

### Learning Test
- Test Method: `cholog.ServiceMockTest.testWithMock`
- Task: Write a test for `MemberService.create` using `@Mock`, `@InjectMocks` with mock verification.

<br>

## 4.2. Testing with Real Repository

<br>

Using `@SpringBootTest` with `@Transactional` allows testing with real repository implementations.

<br>

```kotlin
@SpringBootTest
@Transactional
class ServiceIntegrationTest {
    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun testWithRealRepository() {
        // When
        val result = memberService.create("John", "john@example.com")

        // Then
        assertThat(result.id).isNotNull()
        assertThat(memberRepository.findById(result.id!!)).isNotNull()
    }
}
```

<br>

**Benefits**: Tests real integration, catches more bugs, closer to production behavior
**Drawbacks**: Slower execution, requires database setup, harder to isolate failures

<br>

### Learning Test
- Test Method: `cholog.ServiceIntegrationTest.testWithRealRepository`
- Task: Write an integration test for `MemberService.create` using real repository.

<br>

## 4.3. Testing with @TestConfiguration

<br>

`@TestConfiguration` allows you to provide test-specific bean configurations.

<br>

```kotlin
@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun inMemoryMemberRepository(): MemberRepository = InMemoryMemberRepository()
}

@SpringBootTest
@Import(TestConfig::class)
class ServiceTestConfigurationTest {
    @Autowired
    lateinit var memberService: MemberService

    @Test
    fun testWithTestConfiguration() {
        val result = memberService.create("John", "john@example.com")
        assertThat(result.id).isNotNull()
    }
}
```

<br>

**Benefits**: Fast execution, real service logic, isolated test data, customizable behavior
**Drawbacks**: Requires additional implementation, may differ from production behavior

<br>

### Learning Test
- Test Method: `cholog.ServiceTestConfigurationTest.testWithTestConfiguration`
- Task: Write a test using `@TestConfiguration` with `InMemoryMemberRepository`.

<br>

### Reference
- [Spring - Testing Service Layer](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.mocking-beans)

<br>

# 5. Think About It

- Compare the three service testing approaches. When would you use each one?
- What are the trade-offs between test execution speed and test reliability?
- How do you balance unit tests vs integration tests in your test strategy?

<br>

# 6. Reference
- [Spring Boot - Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Spring - MockMvc](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework)
- [Spring - @JdbcTest](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/jdbc/JdbcTest.html)
