<script setup lang="ts">
import { ref } from 'vue'
import { useTheme } from 'vuetify'


// Theme-Setup
const theme = useTheme()
const appTitle = ref('HeimAG Datenanalysetool')
const appSubtitle = ref('by Renator Huber & Marcel Gräub')
const drawer = ref(false) // Standardmäßig eingeklappt

// Funktion zum Umschalten des Themas
function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}

function logout() {
  location.href = '/login'
}


</script>

<template>
  <v-app>
    <v-responsive class="border rounded">
      <v-navigation-drawer v-model="drawer">
        <v-list-item :title="appTitle" :subtitle="appSubtitle" />
        <v-divider :thickness="2" class="border-opacity-100"></v-divider>
        <br>
        <v-list-item link to="/">Datenanalyse</v-list-item>
        <v-list-item link to="/Settings">Einstellungen</v-list-item>
      </v-navigation-drawer>
      <v-app-bar class="px-3">
        <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
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

