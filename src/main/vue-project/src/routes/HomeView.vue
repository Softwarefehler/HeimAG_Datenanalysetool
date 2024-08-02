<script setup lang="ts">
import { ref } from 'vue'
import { VDateInput } from 'vuetify/labs/VDateInput'

// Typen definieren
export type DatesCategory1 = { date: string; temperatur: string }


const items = [
  {
    date: 'African Elephant',
    temperature: 'Loxodonta africana'
  }
  // ... more items
]


// Reaktive Variablen für die Date-Picker
const startDate = ref<string | null>(null)
let endDate = ref<string | null>(null)
let selectedCountry = ref<string | null>(null)

async function sendData() {
if (startDate.value !==  null  && endDate.value !==  null && selectedCountry.value !==  null) {
//  const startDateData = new FormData()
  // const endDateData = new FormData()
  //  const selectedCountryData = new FormData()

  // startDateData.append('startDate',startDate.value.toString())
//  endDateData.append('endDate', endDate.value.toString())
//  selectedCountryData.append('selectedCountry', selectedCountry.value.toString())

  /*  const responseStartDate = await fetch('/', {
    method: 'POST',
    body: startDateData
  })
  const responseEndDate = await fetch('/', {
    method: 'POST',
    body: endDateData
  })

  const responseSelectedCountry = await fetch('/', {
    method: 'POST',
    body: selectedCountryData
  })


  if (!responseStartDate.ok || !responseEndDate.ok || responseSelectedCountry.ok ) {
    alert(responseStartDate.statusText)
  }else{alert('Bitte füllen Sie alle Felder aus.')}
  }*/
  const formData = new FormData()
  formData.append('startDate', startDate.value)
  formData.append('endDate', endDate.value)
  formData.append('selectedCountry', selectedCountry.value)

  const response = await fetch('/', {
    method: 'POST',
    body: formData
  })

  if (!response.ok) {
    alert(response.statusText)
  } else {
    alert('Daten erfolgreich gesendet!')
  }
} else {
  alert('Bitte füllen Sie alle Felder aus.')
}
}

</script>

<template>
  <v-container>
    <h4 class="text-h5 font-weight-bold mb-4">Suchkriterien</h4>
    <v-select
      v-model="selectedCountry"
      label="Select"
      :items="['California', 'Colorado', 'Florida', 'Georgia', 'Texas', 'Wyoming']"
    ></v-select>
    <v-row dense>
      <v-col cols="12" md="6">
        <v-date-input
          v-model="startDate"
          label="Select a date"
          prepend-icon=""
          prepend-inner-icon="$calendar"
        ></v-date-input>
      </v-col>
      <v-col cols="12" md="6">
        <v-date-input
          v-model="endDate"
          label="Select a date"
          prepend-icon=""
        ></v-date-input>
      </v-col>
    </v-row>
    <v-btn @click="sendData">Send Data</v-btn>
    <br>
    <br>
    <v-row>
      <v-col cols="12" md="12">
        <v-sheet class="mb-4">
          <v-data-table :items="items"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="12">
        <v-sheet class="mb-4">
          <v-data-table :items="items"></v-data-table>
        </v-sheet>
      </v-col>
      <v-col cols="12" md="12">
        <v-sheet>
          <v-data-table :items="items"></v-data-table>
        </v-sheet>
      </v-col>
    </v-row>


  </v-container>
</template>

<style scoped>

</style>