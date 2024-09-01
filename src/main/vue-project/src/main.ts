import { createApp } from 'vue'
import App from './App.vue'
import { router } from '@/routes/router'
import { vuetify } from './vuetify'
import { createVuetify } from 'vuetify'
import { VDateInput } from 'vuetify/labs/components'

export default createVuetify({
  components: {
    VDateInput,
  },
})


createApp(App).use(router).use(vuetify).mount('#app')
