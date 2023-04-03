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

import http from "@/util/http";
import {ICategory, IPage, IResult, IUser} from "@/util/model";

export const get = async <T>(url: string): Promise<IResult<T>> => {
    return (await http.get(url)).data
}

export const post = async <T>(url: string, data: any = null): Promise<IResult<T>> => {
    return (await http.post(url, data)).data
}

export const patch = async <T>(url: string, data: any): Promise<IResult<T>> => {
    return (await http.patch(url, data)).data
}

export const del = async <T>(url: string): Promise<IResult<T>> => {
    return (await http.delete(url)).data
}

export const getUser = async (id: string | null): Promise<IResult<IUser>> => {
    return (await get(`/merchant/user/${id}`))
}

export const isLogin = async (): Promise<IResult<boolean>> => {
    return (await get(`/merchant/auth/isLogin`))
}

export const getCategories = async (page: number, user: string | null): Promise<IResult<IPage<ICategory>>> => {
    return (await get(`/commodity/category?page=${page}&user=${user}`))
}