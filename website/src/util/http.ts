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

import axios from "axios";
import {ElMessage} from "element-plus";
import {useUserStatus} from "@/store";

const http = axios.create({
    baseURL: 'http://localhost:8080/api'
})

http.interceptors.request.use(config => {

    if (useUserStatus().token) {
        if (typeof config.headers?.set === 'function') {
            config.headers.set('token', useUserStatus().token);
        }
    }

    return config
}, error => Promise.reject(error))

http.interceptors.response.use(response => {
    if (response.data.code === 2004) {
        window.$router.push({path: "/login"})
    }

    if (response.data.code != 200) {
        ElMessage({message: response.data.message, type: "error"})
    }

    return response
}, error => {
    ElMessage({message: error.message ?? "Request Blocked", type: "error"})
    Promise.reject(error).then(r => r)
})

export default http
