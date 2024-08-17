<script lang="ts" setup>
import { onMounted, ref } from 'vue'

// Reaktive Variablen für die Datenbankinformationen und den Dateiupload
const databaseStatus = ref<string | null>(null)
const latestDate = ref<string | null>(null)
const file = ref<File | null>(null)
const fileInputError = ref<string | null>(null)
const isLoading = ref<boolean>(false) // Variable für den Ladezustand
const uploadMessage = ref<string | null>(null)
const uploadError = ref<boolean>(false) // Variable für Fehlerstatus

// Validierungsregel für das Dateifeld
const requiredRule = (value: File | null) => {
  return !!value || 'Dieses Feld ist erforderlich'
}

// Funktion zum Abrufen der Datenbankinformationen
async function infoDatabase() {
  try {
    const info = await fetch('/get-SettingsView').then((response) => response.json())
    databaseStatus.value = info.databaseStatus
    latestDate.value = info.latestDate
  } catch (error) {
    alert(`Fehler beim Laden der Daten`)
  }
}

// Funktion zum Hochladen der Datei
async function uploadFile() {
  fileInputError.value = null
  uploadMessage.value = null
  isLoading.value = true // Ladezustand setzen
  uploadError.value = false // Fehlerstatus zurücksetzen

  if (!file.value) {
    fileInputError.value = 'Bitte wählen Sie eine Datei aus'
    isLoading.value = false // Ladezustand beenden, falls kein Upload gestartet wird
    return
  }

  const formData = new FormData()
  formData.append('file', file.value)

  try {
    const response = await fetch('/csv-upload', {
      method: 'POST',
      body: formData,
      headers: {
        'Accept': 'application/json'
      }
    })

    if (response.ok) {
      uploadMessage.value = 'Datei erfolgreich hochgeladen'
    } else {
      uploadMessage.value = 'Fehler beim Hochladen der Datei.'
      uploadError.value = true // Fehlerstatus setzen
    }
  } catch (error) {
    uploadMessage.value = `Anmeldezeit ist Abgelaufen: ${error}`
    uploadError.value = true // Fehlerstatus setzen
    location.href = '/login'
  } finally {
    isLoading.value = false // Ladezustand beenden, wenn der Upload abgeschlossen ist
    await infoDatabase()
  }
}

// Funktion zum Zurücksetzen der Upload-Nachricht und des Fehlerstatus, wenn die Datei entfernt wird
function handleFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  if (!input.files?.length) {
    uploadMessage.value = null // Nachricht zurücksetzen, wenn keine Datei ausgewählt ist
    uploadError.value = false // Fehlerstatus zurücksetzen
  }
}

// Funktion wird beim Laden der Komponente ausgeführt
onMounted(async () => {
  await infoDatabase()
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
        :style="{ marginLeft: '10px' }"
      >
        {{ latestDate }}
      </v-chip>
    </div>
    <v-spacer class="my-6"></v-spacer>
    <v-divider class="my-7" :style="{ height: '20px'}"></v-divider>
    <v-spacer class="my-6"></v-spacer>
    <h4>Datenbankerweiterung / CSV-File Upload</h4>
    <br>
    <v-form @submit.prevent="uploadFile">
      <v-file-input
        v-model="file"
        label="Wähle eine Datei"
        accept=".csv"
        prepend-icon=""
        prepend-inner-icon="mdi-upload"
        :rules="[requiredRule]"
        :error="!!fileInputError || uploadError"
        :error-messages="fileInputError || (uploadError ? 'Fehler beim Hochladen der Datei.' : '')"
        required
        @change="handleFileChange"
      ></v-file-input>

      <!-- Progress Linear -->
      <v-progress-linear
        v-if="isLoading"
        color="primary"
        height="6"
        indeterminate
        class="my-4"
      ></v-progress-linear>

      <!-- Nachricht, die nach dem Upload angezeigt wird -->
      <v-alert
        v-if="uploadMessage"
        :type="uploadError ? 'error' : 'success'"
        dismissible
      >
        {{ uploadMessage }}
      </v-alert>
      <br>
      <v-btn type="submit" color="green lighten-3">Datei hochladen</v-btn>
    </v-form>
  </v-container>
</template>