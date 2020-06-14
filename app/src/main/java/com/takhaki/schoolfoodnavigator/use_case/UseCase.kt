package com.takhaki.schoolfoodnavigator.use_case

import com.takhaki.schoolfoodnavigator.entity.Company
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import com.takhaki.schoolfoodnavigator.repository.AssessmentRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.CompanyRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.ShopRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.UserRepositoryContract
import io.reactivex.Flowable
import io.reactivex.Single

class UseCase(
    private val userRepository: UserRepositoryContract,
    private val companyRepository: CompanyRepositoryContract,
    private val shopRepository: ShopRepositoryContract,
    private val assessmentRepository: AssessmentRepositoryContract
) : UseCaseContract {
    override val company: Flowable<Company>
        get() = TODO("Not yet implemented")
    override val companyId: Int
        get() = TODO("Not yet implemented")

    override fun setCompanyId(id: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteCompanyId() {
        TODO("Not yet implemented")
    }

    override fun joinTeam(): Single<Unit> {
        TODO("Not yet implemented")
    }

    override fun createCompanyRoom(name: String): Single<Int> {
        TODO("Not yet implemented")
    }

    override fun searchCompany(expectId: Int): Single<Boolean> {
        TODO("Not yet implemented")
    }

    override val currentUser: Flowable<UserEntity>
        get() = TODO("Not yet implemented")
    override val users: Flowable<List<UserEntity>>
        get() = TODO("Not yet implemented")

    override fun user(id: String): Flowable<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun signInUser(): Single<Unit> {
        TODO("Not yet implemented")
    }

}