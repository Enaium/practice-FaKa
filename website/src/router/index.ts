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

import {createRouter, createWebHistory} from 'vue-router'
import Home from "../layouts/Home.vue";
import Welcome from "../pages/home/Welcome.vue";
import Feature from "@/pages/home/Feature.vue";
import Contact from "@/pages/home/Contact.vue";
import Register from "@/pages/Register.vue";
import Login from "@/pages/Login.vue";
import Purchase from "@/pages/Purchase.vue";
import Backstage from "@/layouts/Backstage.vue";
import Summarize from "@/pages/backstage/Summarize.vue";
import UserInfo from "@/pages/backstage/UserInfo.vue";
import AddCategory from "@/pages/backstage/AddCategory.vue";
import Categories from "@/pages/backstage/Categories.vue";
import AddGoods from "@/pages/backstage/AddGoods.vue";
import Goods from "@/pages/backstage/Goods.vue";
import AddKami from "@/pages/backstage/AddKami.vue";
import Kamis from "@/pages/backstage/Kamis.vue";
import CreateStore from "@/pages/backstage/CreateStore.vue";
import StoreInfo from "@/pages/backstage/StoreInfo.vue";
import AlipayReturn from "@/pages/AlipayReturn.vue";
import Merchant from "@/pages/Merchant.vue";
import Orders from "@/pages/backstage/Orders.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            component: Home,
            redirect: {name: "welcome"},
            children: [
                {
                    name: "welcome",
                    path: "welcome",
                    component: Welcome
                },
                {
                    name: "feature",
                    path: "feature",
                    component: Feature
                },
                {
                    name: "contact",
                    path: "contact",
                    component: Contact
                }
            ]
        },
        {
            path: "/backstage",
            component: Backstage,
            redirect: {name: "summarize"},
            children: [
                {
                    name: "summarize",
                    path: "summarize",
                    component: Summarize
                },
                {
                    name: "user-info",
                    path: "user-info",
                    component: UserInfo
                },
                {
                    name: "add-category",
                    path: "add-category",
                    component: AddCategory
                },
                {
                    name: "categories",
                    path: "categories",
                    component: Categories
                },
                {
                    name: "add-goods",
                    path: "add-goods",
                    component: AddGoods
                },
                {
                    name: "goods",
                    path: "goods",
                    component: Goods
                },
                {
                    name: "add-kami",
                    path: "add-kami",
                    component: AddKami
                },
                {
                    name: "kamis",
                    path: "kamis",
                    component: Kamis
                },
                {
                    name: "create-store",
                    path: "create-store",
                    component: CreateStore
                },
                {
                    name: "store-info",
                    path: "store-info",
                    component: StoreInfo
                },
                {
                    name: "orders",
                    path: "orders",
                    component: Orders
                }
            ]
        },
        {
            name: "login",
            path: "/login",
            component: Login
        },
        {
            name: "register",
            path: "/register",
            component: Register
        },
        {
            name: "purchase",
            path: "/purchase/:path",
            props: true,
            component: Purchase,
        },
        {
            name: "alipay-return",
            path: "/alipay-return",
            component: AlipayReturn
        },
        {
            name: "merchant",
            path: "/merchant/:id",
            props: true,
            component: Merchant
        }
    ]
})


export default router