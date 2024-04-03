import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {OpenAPI} from "@/api";

OpenAPI.BASE = "http://localhost:8080"

const app = createApp(App)

app.use(router)

app.mount('#app')
