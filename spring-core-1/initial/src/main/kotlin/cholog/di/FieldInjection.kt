package cholog.di

import org.springframework.stereotype.Service

@Service
class FieldInjection {
    // TODO: Inject 'InjectBean' by field injection
    private val injectionBean: InjectionBean? = null

    fun sayHello(): String {
        return injectionBean!!.hello()
    }
}
