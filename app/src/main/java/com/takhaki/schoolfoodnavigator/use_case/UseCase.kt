package com.takhaki.schoolfoodnavigator.use_case

import com.takhaki.schoolfoodnavigator.entity.Company
import com.takhaki.schoolfoodnavigator.entity.UserEntity
import com.takhaki.schoolfoodnavigator.repository.AssessmentRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.CompanyRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.ShopRepositoryContract
import com.takhaki.schoolfoodnavigator.repository.UserRepositoryContract
import kotlinx.coroutines.flow.Flow

class UseCase(
    private val userRepository: UserRepositoryContract,
    private val companyRepository: CompanyRepositoryContract,
    private val shopRepository: ShopRepositoryContract,
    private val assessmentRepository: AssessmentRepositoryContract
) : UseCaseContract {
    override val company: Flow<Company>
        get() = TODO("Not yet implemented")
    override val companyId: Int
        get() = TODO("Not yet implemented")

    override fun setCompanyId(id: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteCompanyId() {
        TODO("Not yet implemented")
    }

    override fun joinTeam() {
        TODO("Not yet implemented")
    }

    override fun createCompanyRoom(name: String): Int {
        TODO("Not yet implemented")
    }

    override fun searchCompany(expectId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override val currentUser: Flow<UserEntity>
        get() = TODO("Not yet implemented")
    override val users: Flow<List<UserEntity>>
        get() = TODO("Not yet implemented")

    override fun user(id: String): Flow<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun signInUser() {
        TODO("Not yet implemented")
    }

}