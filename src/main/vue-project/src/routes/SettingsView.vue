
<script lang="ts" setup>
import { onMounted, ref } from 'vue'


const databaseStatus  = ref<string | null>(null)
const latestDate = ref<string | null>(null)
const file = ref<File | null>(null)

async function infoDatabase() {
  try {
    const info = await fetch('/get-SettingsView').then((response) => response.json())
    databaseStatus.value = info.databaseStatus
    latestDate.value = info.latestDate
  } catch (error) {
    alert(`Fehler beim Laden der Daten: ${error}`)
  }
}


async function uploadFile() {
  if (!file.value) {
    alert('Bitte wähle eine Datei aus.')
    return
  }

  const formData = new FormData()
  formData.append('file', file.value)

  try {
    const response = await fetch('/csv-upload', {
      method: 'POST',
      body: formData,
      headers: {
        'Accept': 'application/json',
      },
    })

    if (response.ok) {
      const result = await response.json()
      alert('Datei erfolgreich hochgeladen!')
    } else {
      alert('Fehler beim Hochladen der Datei.')
    }
  } catch (error) {
    console.error('Fehler beim Hochladen der Datei:', error)
    alert('Fehler beim Hochladen der Datei.')
  }
}


onMounted(async () => {
  await infoDatabase()
})
</script>

<template>
  <v-container>
    <v-form @submit.prevent="uploadFile">
      <v-file-input
        v-model="file"
        label="Wähle eine Datei"
        accept=".csv"
        required
      ></v-file-input>
      <v-btn type="submit" color="primary">Datei hochladen</v-btn>
    </v-form>
  </v-container>
</template>
