<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { VDateInput } from 'vuetify/labs/VDateInput'
import { VSelect, VRow, VCol, VContainer, VBtn, VSheet, VDataTable } from 'vuetify/components'


export type dataPoint = { date: string; temperature: string }


// Reaktive Variablen für die Date-Picker
const startDate = ref<string | null>(null)
const endDate = ref<string | null>(null)
const selectedCountry = ref<string | null>(null)
const tableData = ref<dataPoint[]>([])

const headers = ref([
  { text: 'Datum', value: 'date' },
  { text: 'Temperatur', value: 'temperature' }
])

async function sendData() {
  if (startDate.value !== null && endDate.value !== null && selectedCountry.value !== null) {
    const formData = new FormData()
    formData.append('startDate', startDate.value)
    formData.append('endDate', endDate.value)
    formData.append('selectedCountry', selectedCountry.value)

    try {
      const response = await fetch('/', {
        method: 'POST',
        body: formData
      })
      if (!response.ok) {
        alert(response.statusText)
      } else {
        const payload = await response.json()
        processPayload(payload)
        //  fetchDataPointsFromServer()       const payload = await response.json()
      }
    } catch (error) {
      alert(`Fehler: ${error}`)
    }
  } else {
    alert('Bitte füllen Sie alle Felder aus.')
  }
}

function processPayload(payload: any) {
  // Annahme: payload ist ein Array von Objekten, die die Struktur von dataPoint haben
  tableData.value = payload.map((item: any) => ({
    Datum: item.date,
    Temperatur: item.temperature
  }))
}

/*
function fetchDataPointsFromServer() {
  fetch('/')
    .then((response) => response.json())
    .then((payload) => {
      tableData.value = payload
    })
}


onMounted(fetchDataPointsFromServer)*/
</script>

<template>
  <v-container>
    <h4 class="text-h5 font-weight-bold mb-4">Suchkriterien</h4>
    <v-select
      v-model="selectedCountry"
      label="Wähle einen Ort aus"
      :items="['California', 'Colorado', 'Florida', 'Georgia', 'Texas', 'Wyoming']"
    ></v-select>
    <v-row dense>
      <v-col cols="12" md="6">
        <v-date-input
          v-model="startDate"
          label="Wähle das Startdatum"
          prepend-icon=""
          prepend-inner-icon="$calendar"
        ></v-date-input>
      </v-col>
      <v-col cols="12" md="6">
        <v-date-input
          v-model="endDate"
          label="Wähle das Enddatum"
          prepend-icon="$calendar"
        ></v-date-input>
      </v-col>
    </v-row>
    <v-btn @click="sendData">Send Data</v-btn>
    <br>
    <br>
    <v-row>
      <v-col cols="12" md="12">
        <v-sheet class="mb-4">
          <v-data-table :items="tableData"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="12">
        <v-sheet class="mb-4">
          <v-data-table :items="tableData"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="12">
        <v-sheet class="mb-4">
          <v-data-table  :items="tableData"></v-data-table>
        </v-sheet>
      </v-col>
    </v-row>


  </v-container>
</template>

<style scoped>

</style>