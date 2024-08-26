<script lang="ts" setup>
import { onMounted, ref } from 'vue'


const latestDate = ref<string | null>(null)
const csvFile = ref<File | null>(null)
const csvFileInputError = ref<string | null>(null)
const csvIsLoading = ref<boolean>(false) // Variable für den Ladezustand
const csvUploadMessage = ref<string | null>(null)
const csvUploadError = ref<boolean>(false) // Variable für Fehlerstatus

// Validierung
const csvRequiredRule = (value: File | null) => {
  return !!value || 'Dieses Feld ist erforderlich'
}


// Funktion zum Hochladen der Datei
async function csvUploadFile() {
  csvFileInputError.value = null
  csvUploadMessage.value = null
  csvIsLoading.value = true
  csvUploadError.value = false

  if (!csvFile.value) {
    csvFileInputError.value = 'Bitte wählen Sie eine Datei aus'
    csvIsLoading.value = false
    return
  }

  if (csvFile.value.name !== 'wetterdaten.csv') {
    csvFileInputError.value = 'Die Datei muss den Namen "wetterdaten.csv" haben.'
    csvIsLoading.value = false
    return
  }

  const formData = new FormData()
  formData.append('file', csvFile.value)

  try {
    const response = await fetch('/csv-upload', {
      method: 'POST',
      body: formData,
      headers: {
        'Accept': 'application/json'
      }
    })

    if (response.ok) {
      csvUploadMessage.value = 'Datei erfolgreich hochgeladen'
    } else {
      csvUploadMessage.value = 'Fehler beim Hochladen der Datei.'
      csvUploadError.value = true
    }
  } catch (error) {
    csvUploadMessage.value = 'Anmeldezeit ist Abgelaufen: ${error}'
    csvUploadError.value = true
    location.href = '/login'
  } finally {
    csvIsLoading.value = false
    await firstPayload()
  }
}


// Funktion zum Zurücksetzen der Upload-Nachricht / Fehlerstatus, wenn die Datei entfernt wird
function csvHandleFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  if (!input.files?.length) {
    csvUploadMessage.value = null
    csvUploadError.value = false
    return
  }
  csvFile.value = input.files[0]

  // Überprüfen des Dateinamens
  if (csvFile.value.name !== 'wetterdaten.csv') {
    csvFileInputError.value = 'Die Datei muss den Namen "wetterdaten.csv" haben.'
    csvUploadError.value = true
    csvFile.value = null
  } else {
    csvFileInputError.value = null
    csvUploadError.value = false
  }
}


async function firstPayload() {
  try {
    const info = await fetch('/settingsView').then((response) => response.json())
    latestDate.value = info.latestDate
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
    <h4>Datenbankinformation</h4>
    <br>
    <div class="d-flex align-center flex-wrap">
      <p>Aktuellstes Datum in der Datenbank:</p>
      <v-chip
        color="green darken-2"
        text-color="white"
        :style="{ marginLeft: '10px' }">
        {{ latestDate }}
      </v-chip>
    </div>
    <v-spacer class="my-6"></v-spacer>
    <v-divider class="my-7" :style="{ height: '20px'}"></v-divider>
    <v-spacer class="my-6"></v-spacer>
    <h4>Datenbankerweiterung / CSV-File Upload</h4>
    <br>
    <v-form @submit.prevent="csvUploadFile">
      <v-file-input
        v-model="csvFile"
        label="Wähle die Datei wetterdaten.csv"
        accept="wetterdaten.csv"
        prepend-icon=""
        prepend-inner-icon="mdi-upload"
        :rules="[csvRequiredRule]"
        :error="!!csvFileInputError || csvUploadError"
        :error-messages="csvFileInputError || (csvUploadError ? 'Fehler beim Hochladen der Datei.' : '')"
        required
        @change="csvHandleFileChange"
      ></v-file-input>
      <v-progress-linear
        v-if="csvIsLoading"
        color="primary"
        height="6"
        indeterminate
        class="my-4"
      ></v-progress-linear>
      <v-alert
        v-if="csvUploadMessage"
        :type="csvUploadError ? 'error' : 'success'"
        dismissible>
        {{ csvUploadMessage }}
      </v-alert>
      <br>
      <v-btn type="submit" color="green lighten-3">Datei hochladen</v-btn>
    </v-form>
  </v-container>
</template>