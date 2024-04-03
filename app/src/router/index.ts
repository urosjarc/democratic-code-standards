import {createRouter, createWebHistory} from 'vue-router'
import IndexView from "@/views/IndexView.vue";
import LoginView from "@/views/LoginView.vue";
import {OpenAPI, UserService} from "@/api";
import UserView from "@/views/UserView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'index',
            component: IndexView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/user',
            name: 'user',
            component: UserView
        },
    ]
})
router.beforeEach(async (to, from) => {
    let token = sessionStorage.getItem("token")
    OpenAPI.TOKEN = token || undefined
    if (to.path != "/login") {
        try {
            await UserService.getUser()
            return true
        } catch (e) {
            await router.push({path: "/login"})
        }
        return true
    } else {
        try {
            await UserService.getUser()
            await router.push({path: "/user"})
        } catch (e){
            return true
        }
    }
})

export default router
