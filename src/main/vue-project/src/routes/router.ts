import { createRouter, createWebHashHistory } from 'vue-router'
import HomeVue from '@/routes/HomeView.vue'

const routes = [
 { path: '/', component: HomeVue },
]

export const router = createRouter({
  routes,
  history: createWebHashHistory()
})
