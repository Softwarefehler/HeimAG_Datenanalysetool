<script setup lang="ts">
import { ref } from 'vue'
import type { PropType } from 'vue'
import { VDateInput } from 'vuetify/labs/VDateInput'

// Typen definieren
export type DatesCategory1 = { date: string; temperatur: string }


// Props-Definition
defineProps({
  fotos: {
    default: null,
    required: false,
    type: Array as PropType<DatesCategory1[]>
  }
})
const items = [
  {
    date: 'African Elephant',
    temperature: 'Loxodonta africana'
  }
  // ... more items
]

// Reaktive Variablen für die Date-Picker
const startDate = ref<string | null>(null)
const endDate = ref<string | null>(null)

</script>

<template>
  <v-container>
    <h4 class="text-h5 font-weight-bold mb-4">Suchkriterien</h4>
    <v-select
      label="Select"
      :items="['California', 'Colorado', 'Florida', 'Georgia', 'Texas', 'Wyoming']"
    ></v-select>
    <v-row dense>
      <v-col cols="12" md="6">
        <v-date-input
          label="Select a date"
          prepend-icon=""
          prepend-inner-icon="$calendar"
        ></v-date-input>
      </v-col>
      <v-col cols="12" md="6">
        <v-date-input
          label="Select a date"
          prepend-icon=""
        ></v-date-input>
      </v-col>
    </v-row>
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