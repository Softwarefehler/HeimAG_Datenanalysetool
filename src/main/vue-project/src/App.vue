<script setup lang="ts">
import {  ref } from 'vue'
import { useTheme } from 'vuetify'



// Theme-Setup
const theme = useTheme()
const appTitle = ref('HeimAG Datenanalysetool')

// Funktion zum Umschalten des Themas
function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

function logout() {
  location.href = '/logout'
}


</script>

<template>
    <v-app>
      <v-responsive class="border rounded">
      <v-app-bar class="px-3">
        <v-app-bar-title>{{ appTitle }}</v-app-bar-title>
        <v-btn @click="toggleTheme" icon="mdi-theme-light-dark" />
        <v-btn @click="logout" icon="mdi-logout" />
      </v-app-bar>
      <v-main>
        <v-container>
          <RouterView />
        </v-container>
      </v-main>
      </v-responsive>
    </v-app>
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

