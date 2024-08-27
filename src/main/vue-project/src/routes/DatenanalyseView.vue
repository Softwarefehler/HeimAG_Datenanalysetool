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

const startDate = ref<string | null>(null)
const endDate = ref<string | null>(null)
const selectedCountry = ref<string | null>(null)
const tableData1 = ref<DataPoint[]>([])
const tableData2 = ref<DataPoint[]>([])
const tableData3 = ref<DataPoint[]>([])
const countryList = ref<string[]>([])
const startDateError = ref<string | null>(null)
const endDateError = ref<string | null>(null)
const selectedCountryError = ref<string | null>(null)

// validate
const requiredRule = (value: any) => !!value || 'Dieses Feld ist erforderlich'


// search data where in the range of the parameters
async function searchData() {
  startDateError.value = null
  endDateError.value = null
  selectedCountryError.value = null

  if (!startDate.value) {
    startDateError.value = 'Bitte wählen Sie ein Startdatum'
  }
  if (!endDate.value) {
    endDateError.value = 'Bitte wählen Sie ein Enddatum'
  }
  if (!selectedCountry.value) {
    selectedCountryError.value = 'Bitte wählen Sie einen Ort aus'
  }

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
      alert(`Anmeldezeit ist Abgelaufen`)
      location.href = '/login'
    }
  }
}


// Split the Payload to the tables
function processPayload(payload: PayloadType) {
  tableData1.value = payload.kaltPeriode
  tableData2.value = payload.hauptanteilHeizperiode
  tableData3.value = payload.schwachlast
}


// The First Payload from this side
async function firstPayload() {
  try {
    const data = await fetch('/datenanalyseView').then((response) => response.json())
    countryList.value = data.countryList
  } catch (error) {
    alert(`Die Anmeldezeit ist abgelaufen`)
    location.href = '/login'
  }
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
      :rules="[requiredRule]"
      :error="!!selectedCountryError"
      :error-messages="selectedCountryError"
    ></v-select>
    <v-row dense>
      <v-col cols="12" md="6">
        <v-date-input
          v-model="startDate"
          label="Wähle das Startdatum"
          prepend-icon=""
          prepend-inner-icon="$calendar"
          :rules="[requiredRule]"
          :error="!!startDateError"
          :error-messages="startDateError"
        ></v-date-input>
      </v-col>
      <v-col cols="12" md="6">
        <v-date-input
          v-model="endDate"
          label="Wähle das Enddatum"
          prepend-icon=""
          prepend-inner-icon="$calendar"
          :rules="[requiredRule]"
          :error="!!endDateError"
          :error-messages="endDateError"
        ></v-date-input>
      </v-col>
    </v-row>
    <br>
    <v-row align="center">
      <v-col cols="auto">
        <v-btn @click="searchData" color="green lighten-3">Suche starten</v-btn>
      </v-col>
      <v-spacer></v-spacer>
      <v-col cols="auto">
        <v-btn :to="{ path: '/swisstopo' }" color="green lighten-3">Karte</v-btn>
      </v-col>
    </v-row>
    <v-spacer class="my-6"></v-spacer>
    <v-divider class="my-7" :style="{ height: '20px'}"></v-divider>
    <v-spacer class="my-6"></v-spacer>
    <v-row>
      <v-col cols="12" md="4">
        <v-sheet class="mb-4">
          <h4 class="text-h6 font-weight-bold mb-1">Kalte Periode</h4>
          <p class="text-body-2 mb-2" style="margin-top: -8px;">Temperaturen zwischen -10 ... -5°C</p>
          <v-data-table :items="tableData1" :items-per-page="-1" class="custom-table"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="4">
        <v-sheet class="mb-4">
          <h4 class="text-h6 font-weight-bold mb-1">Hauptanteil Heizperiode</h4>
          <p class="text-body-2 mb-2" style="margin-top: -8px;">Temperaturen zwischen 0 ... 20°C</p>
          <v-data-table :items="tableData2" :items-per-page="-1" class="custom-table"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="4">
        <v-sheet class="mb-4">
          <h4 class="text-h6 font-weight-bold mb-1">Schwachlast</h4>
          <p class="text-body-2 mb-2" style="margin-top: -8px;">Temperatur möglichst nahe bei 20°C</p>
          <v-data-table :items="tableData3" :items-per-page="-1" class="custom-table"></v-data-table>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

