package com.takhaki.schoolfoodnavigator.module

import com.takhaki.schoolfoodnavigator.repository.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<ShopRepositoryContract> { ShopRepository(androidContext()) }

    factory<AssessmentRespositoryContract> { AssessmentRepository(get(), androidContext()) }

    factory<UserRepositoryContract> { UserRepository(androidContext()) }
}