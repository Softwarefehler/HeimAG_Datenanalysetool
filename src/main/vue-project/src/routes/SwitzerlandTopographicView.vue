<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { VBtn } from 'vuetify/components'


export type Foto = { url: string; name: string }

const foto = ref<Foto | null>(null)


// Da Bild statisch hinterlegt ist und der Fehler passiert, melden wir uns ab
async function logout() {
  alert(`Die Anmeldezeit ist abgelaufen`)
  location.href = '/login'
}


async function fetchFotoFromServer() {
  try {
    const swisstopo = await fetch('/swisstopographic').then((response) => response.blob())
    const url = URL.createObjectURL(swisstopo)
    foto.value = { url, name: 'Switzerland_topographic.png' }
  } catch (error) {
    alert('Die Anmeldezeit ist abgelaufen')
   location.href = '/login'
  }
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
        <img v-if="foto" :src="foto.url" :alt="foto.name" @error=" logout" class="my-image" />
      </v-col>
    </v-row>
    <v-btn :to="{ path: '/' }" color="green lighten-3" class="my-button">Zurück</v-btn>
  </v-container>
</template>

<style scoped>
.my-image {
  display: block; /* Macht das Bild zu einem Blockelement, sodass es eine neue Zeile einnimmt */
  margin: 0 auto; /* Zentriert das Bild horizontal */
  max-width: 100%; /* Stellt sicher, dass das Bild nicht breiter als sein Container ist */
}

/* Stil für den Button */
.my-button {
  margin-top: 20px; /* Abstand oben, damit der Button unterhalb des Bildes ist */
}
</style>
