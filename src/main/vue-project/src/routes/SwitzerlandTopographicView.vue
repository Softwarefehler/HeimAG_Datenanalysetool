<script setup lang="ts">
import { onMounted, ref, type Ref } from 'vue'
import { VBtn } from 'vuetify/components'

export type Foto = { url: string; name: string }

const foto = ref<Foto | null>(null)


function fetchFotoFromServer() {
  fetch('/swisstopographic')
    .then(response => {
      if (!response.ok) throw new Error("Bild konnte nicht geladen werden")
      return response.blob()
    })
    .then(blob => {
      const url = URL.createObjectURL(blob)
      foto.value = { url, name: "Switzerland_topographic.png" }
    })
    .catch(error => console.error('Fehler beim Laden des Bildes:', error))
}

onMounted(fetchFotoFromServer)
</script>


<template>
  <v-container>
    <!-- Flexbox Container für Bild und Text -->
    <v-row class="d-flex align-center">
      <v-col cols="auto" class="d-flex align-center">
        <h4 class="text-h5 font-weight-bold mb-0">Karte Wetterstationen Schweiz</h4>
      </v-col>
      <v-col cols="auto" class="d-flex align-center">
        <!-- Bild -->
        <img v-if="foto" :src="foto.url" :alt="foto.name" class="my-image" />
      </v-col>
    </v-row>

    <!-- Button unter Bild und Text -->
    <v-btn :to="{ path: '/' }" color="green lighten-3" class="my-button">Zurück</v-btn>
  </v-container>
</template>

<style scoped>
/* Stil für das Bild, um sicherzustellen, dass es gut aussieht */
.my-image {
  display: block; /* Macht das Bild zu einem Blockelement, sodass es eine neue Zeile einnimmt */
  margin: 0 auto; /* Zentriert das Bild horizontal */
  max-width: 100%; /* Stellt sicher, dass das Bild nicht breiter als sein Container ist */
}

/* Stil für den Button, um sicherzustellen, dass er gut aussieht */
.my-button {
  margin-top: 20px; /* Abstand oben, damit der Button unterhalb des Bildes ist */
}
</style>
