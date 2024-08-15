
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { VDateInput } from 'vuetify/labs/VDateInput'
import { VSelect, VRow, VCol, VContainer, VBtn, VSheet, VDataTable } from 'vuetify/components'


export type DataPoint = { date: string; temperature: string }

type PayloadType = {
  kaltPeriode: DataPoint[],
  hauptanteilHeizperiode: DataPoint[],
  schwachlast: DataPoint[]
}

// Reaktive Variablen für die Date-Picker
const startDate = ref<string | null>(null)
const endDate = ref<string | null>(null)
const selectedCountry = ref<string | null>(null)
const tableData1 = ref<DataPoint[]>([])
const tableData2 = ref<DataPoint[]>([])
const tableData3 = ref<DataPoint[]>([])

const databaseStatus = ref<string | null>(null)
const countryList = ref<string[]>([])
const latestDate = ref<string | null>(null)



 async function firstPayload() {
  try {
    const data = await fetch('/get-DatenanalyseView').then((response) => response.json())
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



function processPayload(payload: PayloadType) {
  // Verarbeitung der drei Listen
  tableData1.value = payload.kaltPeriode
  tableData2.value = payload.hauptanteilHeizperiode
  tableData3.value = payload.schwachlast
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
    <v-spacer class="my-6"></v-spacer>
    <v-divider class="my-7" :style="{ height: '20px'}"></v-divider>
    <v-spacer class="my-6"></v-spacer>
    <v-row>
      <v-col cols="12" md="4">
        <v-sheet class="mb-4">
          <h4 class="text-h6 font-weight-bold mb-1">Kalte Periode</h4>
          <p class="text-body-2 mb-2" style="margin-top: -8px;">Temperaturen zwischen -5 ... - 10°C</p>
          <v-data-table :items="tableData1" :items-per-page="-1"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="4">
        <v-sheet class="mb-4">
          <h4 class="text-h6 font-weight-bold mb-1">Hauptanteil Heizperiode</h4>
          <p class="text-body-2 mb-2" style="margin-top: -8px;">Temperaturen zwischen 0 ... 20°C</p>
          <v-data-table :items="tableData2" :items-per-page="-1"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="4">
        <v-sheet class="mb-4">
          <h4 class="text-h6 font-weight-bold mb-1">Schwachlast</h4>
          <p class="text-body-2 mb-2" style="margin-top: -8px;">Temperatur möglichst nahe bei 20°C</p>
          <v-data-table :items="tableData3" :items-per-page="-1"></v-data-table>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>


<style scoped>

</style>