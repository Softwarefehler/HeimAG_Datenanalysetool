<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { VBtn } from 'vuetify/components'

export type Foto = { url: string; name: string }

const foto = ref<Foto | null>(null)
const latestDate = ref<string | null>(null)


// Use the Value only to check the authentication
async function checkAuthentication() {
  try {
    const info = await fetch('/settingsView').then((response) => response.json())
    latestDate.value = info.latestDate
  } catch (error) {
    alert(`Die Anmeldezeit ist abgelaufen`)
    location.href = '/login'
  }
}

// Load the foto path from the Backend to the frontend
async function fetchFotoFromServer() {
  try {
    const swisstopo = await fetch('/swisstopographic').then((response) => response.blob())
    const url = URL.createObjectURL(swisstopo)
    foto.value = { url, name: 'Switzerland_topographic.png' }
  } catch (error) {
    // Placeholder
  }
  await checkAuthentication()
}

onMounted(fetchFotoFromServer)
</script>

<template>
  <v-container>
    <v-row class="d-flex align-center">
      <v-col cols="auto" class="d-flex align-center">
        <h4 class="text-h5 font-weight-bold mb-0">Karte Wetterstationen Schweiz</h4>
      </v-col>
      <v-col cols="auto" class="d-flex align-center">
        <img v-if="foto" :src="foto.url" :alt="foto.name" class="my-image" />
      </v-col>
    </v-row>
    <v-btn :to="{ path: '/' }" color="green lighten-3" class="my-button">Zurück</v-btn>
  </v-container>
</template>

<style scoped>
.my-image {
  display: block;
  margin: 0 auto;
  max-width: 100%;
}

.my-button {
  margin-top: 20px;
}
</style>
