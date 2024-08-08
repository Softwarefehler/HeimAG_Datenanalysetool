<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { VDateInput } from 'vuetify/labs/VDateInput'
import { VSelect, VRow, VCol, VContainer, VBtn, VSheet, VDataTable } from 'vuetify/components'


export type FirstResponse = { databaseStatus: string, countryList: Array<string>; latestDate: string }

export type DataPoint = { date: string; temperature: string }


// Reaktive Variablen für die Date-Picker
const startDate = ref<string | null>(null)
const endDate = ref<string | null>(null)
const selectedCountry = ref<string | null>(null)
const tableData = ref<DataPoint[]>([])


const databaseStatus = ref<string | null>(null)
const countryList = ref<string[]>([])
const latestDate = ref<string | null>(null)


 async function firstPayload() {
  try {
    const data = await fetch('/get-data').then((response) => response.json())
    databaseStatus.value = data.databaseStatus
    countryList.value = data.countryList
    latestDate.value = data.latestDate
  } catch (error) {
    alert(`Fehler beim Laden der Daten: ${error}`)
  }
}

async function sendData() {
  if (startDate.value !== null && endDate.value !== null && selectedCountry.value !== null) {
    const formData = new FormData()
    formData.append('startDate', startDate.value)
    formData.append('endDate', endDate.value)
    formData.append('selectedCountry', selectedCountry.value)

    try {
      const response = await fetch('/search', {
        method: 'POST',
        body: formData
      })
      if (!response.ok) {
        alert(response.statusText)
      } else {
        const payload = await response.json()
        processPayload(payload)
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


onMounted(async () => {
  await firstPayload()
})
</script>

<template>
  <v-container>
    <h4 class="text-h5 font-weight-bold mb-4">Suchkriterien</h4>
    <v-select
      v-model="selectedCountry"
      label="Wähle einen Ort aus"
      :items="countryList"
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
          prepend-icon=""
          prepend-inner-icon="$calendar"
        ></v-date-input>
      </v-col>
    </v-row>
    <v-btn @click="sendData">Suche starten</v-btn>
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
          <v-data-table :items="tableData"></v-data-table>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>

</style>