import { createRouter, createWebHashHistory } from 'vue-router'
import DatenanalyseVue from '@/routes/DatenanalyseView.vue'
import SettingsVue from '@/routes/SettingsView.vue'
import Swisstopo from '@/routes/SwitzerlandTopographicView.vue'

const routes = [
 { path: '/', component: DatenanalyseVue },
  { path: '/settings', component: SettingsVue },
  { path: '/swisstopo', component: Swisstopo }
]

export const router = createRouter({
  routes,
  history: createWebHashHistory()
})
