package cn.enaium.faka.controller

import cn.dev33.satoken.stp.StpUtil
import cn.enaium.faka.model.entity.User
import cn.enaium.faka.model.entity.UserInfo
import cn.enaium.faka.model.entity.UserWallet
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.model.post.AuthLoginPost
import cn.enaium.faka.model.post.AuthRegisterPost
import cn.enaium.faka.model.response.PostLoginResponse
import cn.enaium.faka.repository.UserInfoRepository
import cn.enaium.faka.repository.UserRepository
import cn.enaium.faka.repository.UserWalletRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.util.DigestUtil
import org.babyfish.jimmer.kt.new
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

/**
 * @author Enaium
 */
@RestController
@Transactional
@RequestMapping("/auth")
class AuthController(
    val userRepository: UserRepository,
    val userInfoRepository: UserInfoRepository,
    val userWalletRepository: UserWalletRepository
) {
    /**
     * 注册
     */
    @PostMapping("/register")
    fun register(@RequestBody param: AuthRegisterPost): Result<Nothing?> {

        if (param.password != param.reenter) {
            return Result.Builder.fail(status = Result.Status.PASSWORD_NOT_SAME)
        }

        val userId = userRepository.insert(new(User::class).by {
            this.username = param.username
            this.password = DigestUtil.md5(param.password)
        }).id

        userInfoRepository.insert(new(UserInfo::class).by { this.userId = userId })
        userWalletRepository.insert(new(UserWallet::class).by { this.userId = userId })

        return Result.Builder.success()
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    fun login(@RequestBody param: AuthLoginPost): Result<PostLoginResponse?> {
        val findUserByUsername = userRepository.findUserByUsername(param.username)
        findUserByUsername?.let {
            if (DigestUtil.md5(param.password) == it.password) {
                return Result.Builder.success(metadata = PostLoginResponse(it.id, StpUtil.createLoginSession(it.id)))
            }
        }
        return Result.Builder.fail()
    }


    @GetMapping("/isLogin")
    fun isLogin(): Result<Boolean?> {
        return Result.Builder.success(metadata = StpUtil.isLogin())
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    fun logout(): Result<Nothing?> {
        StpUtil.logout()
        return Result.Builder.success()
    }
}