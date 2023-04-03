/*
 * Copyright 2023 Enaium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.enaium.faka.controller

import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.exception.NotRoleException
import cn.enaium.faka.result.Result
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author Enaium
 */
@RestControllerAdvice
class ExceptionController {
    @ExceptionHandler(Exception::class)
    fun exception(exception: Exception): Result<Nothing?> {
        when (exception) {
            is NotLoginException -> {
                return Result.Builder.fail(status = Result.Status.NOT_LOGIN)
            }

            is NotRoleException -> {
                return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
            }

            is MissingServletRequestParameterException -> {
                return Result.Builder.fail(status = Result.Status.PARAMETER_MISSING)
            }

            else -> {
                exception.printStackTrace()
                return Result.Builder.fail()
            }
        }
    }
}