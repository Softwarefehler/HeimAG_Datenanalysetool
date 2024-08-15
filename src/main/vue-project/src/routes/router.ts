import { createRouter, createWebHashHistory } from 'vue-router'
import DatenanalyseVue from '@/routes/DatenanalyseView.vue'
import SettingsVue from '@/routes/SettingsView.vue'

const routes = [
 { path: '/', component: DatenanalyseVue },
  { path: '/settings', component: SettingsVue }
]

export const router = createRouter({
  routes,
  history: createWebHashHistory()
})
