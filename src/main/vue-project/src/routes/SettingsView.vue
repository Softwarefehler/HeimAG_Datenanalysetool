<script lang="ts" setup>
import { onMounted, ref } from 'vue'

// Reaktive Variablen für die Datenbankinformationen und den Dateiupload
const databaseStatus = ref<string | null>(null)
const latestDate = ref<string | null>(null)

// CSV Upload Variablen
const csvFile = ref<File | null>(null)
const csvFileInputError = ref<string | null>(null)
const csvIsLoading = ref<boolean>(false) // Variable für den Ladezustand
const csvUploadMessage = ref<string | null>(null)
const csvUploadError = ref<boolean>(false) // Variable für Fehlerstatus


// Validierungsregel für das Dateifeld
const csvRequiredRule = (value: File | null) => {
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
async function csvUploadFile() {
  csvFileInputError.value = null
  csvUploadMessage.value = null
  csvIsLoading.value = true // Ladezustand setzen
  csvUploadError.value = false // Fehlerstatus zurücksetzen

  if (!csvFile.value) {
    csvFileInputError.value = 'Bitte wählen Sie eine Datei aus'
    csvIsLoading.value = false // Ladezustand beenden, falls kein Upload gestartet wird
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
      csvUploadError.value = true // Fehlerstatus setzen
    }
  } catch (error) {
    csvUploadMessage.value = `Anmeldezeit ist Abgelaufen: ${error}`
    csvUploadError.value = true // Fehlerstatus setzen
    location.href = '/login'
  } finally {
    csvIsLoading.value = false // Ladezustand beenden, wenn der Upload abgeschlossen ist
    await infoDatabase()
  }
}

// Funktion zum Zurücksetzen der Upload-Nachricht und des Fehlerstatus, wenn die Datei entfernt wird
function csvHandleFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  if (!input.files?.length) {
    csvUploadMessage.value = null // Nachricht zurücksetzen, wenn keine Datei ausgewählt ist
    csvUploadError.value = false // Fehlerstatus zurücksetzen
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
    <v-form @submit.prevent="csvUploadFile">
      <v-file-input
        v-model="csvFile"
        label="Wähle die Datei merged_output.csv"
        accept="merged_output.csv"
        prepend-icon=""
        prepend-inner-icon="mdi-upload"
        :rules="[csvRequiredRule]"
        :error="!!csvFileInputError || csvUploadError"
        :error-messages="csvFileInputError || (csvUploadError ? 'Fehler beim Hochladen der Datei.' : '')"
        required
        @change="csvHandleFileChange"
      ></v-file-input>

      <!-- Progress Linear -->
      <v-progress-linear
        v-if="csvIsLoading"
        color="primary"
        height="6"
        indeterminate
        class="my-4"
      ></v-progress-linear>

      <!-- Nachricht, die nach dem Upload angezeigt wird -->
      <v-alert
        v-if="csvUploadMessage"
        :type="csvUploadError ? 'error' : 'success'"
        dismissible
      >
        {{ csvUploadMessage }}
      </v-alert>
      <br>
      <v-btn type="submit" color="green lighten-3">Datei hochladen</v-btn>
    </v-form>

  </v-container>
</template>