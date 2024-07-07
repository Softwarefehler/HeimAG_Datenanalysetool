import { createRouter, createWebHashHistory } from 'vue-router'
import HomeVue from '@/views/Homeview.vue'
import DatenanalyseVUE from '@/views/Datenanalyse.vue'
const routes = [
  { path: '/', component: HomeVue },
  { path: '/datenanalyse', component: DatenanalyseVUE }
]

export const router = createRouter({
  routes,
  history: createWebHashHistory()
})
