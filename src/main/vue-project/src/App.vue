<script setup lang="ts">

import { ref } from 'vue'
import { useTheme } from 'vuetify'


const drawer = ref<boolean | null>(null)
const appTitle = ref('HeimAG Datenanalysetool')
const appSubtitle = ref('NDS SWE Semesterarbeit 2')
const theme = ref('light')

function onClick() {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
}


</script>
<template>
  <v-responsive class="border rounded">
    <v-app :theme="theme">
      <v-app-bar class="px-3">
        <v-spacer></v-spacer>

        <v-btn
          :prepend-icon="theme === 'light' ? 'mdi-weather-sunny' : 'mdi-weather-night'"
          text="light/dark"
          slim
          @click="onClick"
        ></v-btn>
      </v-app-bar>

      <v-main>
        <v-container>
          <h1>Login</h1>
          <p>Benutzername</p>
          <v-responsive
            class="mx-auto"
            max-width="344"
          >
            <v-text-field
              hint="Benutzername eingeben"
              label="Benutzer"
              type="input"
            ></v-text-field>
          </v-responsive>
          <p>Passwort</p>
          <v-responsive
            class="mx-auto"
            max-width="344"
          >
            <v-text-field
              hint="Passwort eingeben"
              label="Passwort"
              type="input"
            ></v-text-field>
          </v-responsive>
        </v-container>
      </v-main>
    </v-app>
  </v-responsive>
</template>


<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
