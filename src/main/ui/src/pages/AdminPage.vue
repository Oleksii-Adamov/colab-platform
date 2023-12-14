<template>
  <AdminNavigation></AdminNavigation>
  <div class="date-range-container">
    <div class="date-selectors">
      <div class="date-select">
        <label for="startMonth">Start Month:</label>
        <select v-model="beginningMonth">
          <option v-for="month in months" :key="month.value" :value="month.value">{{ month.label }}</option>
        </select>
      </div>

      <div class="date-select">
        <label for="startYear">Start Year:</label>
        <select v-model="beginningYear">
          <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
        </select>
      </div>

      <div class="date-select">
        <label for="endMonth">End Month:</label>
        <select v-model="endMonth">
          <option v-for="month in months" :key="month.value" :value="month.value">{{ month.label }}</option>
        </select>
      </div>

      <div class="date-select">
        <label for="endYear">End Year:</label>
        <select v-model="endYear">
          <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
        </select>
      </div>

      <div class="date-select">
        <button @click="retrieveInfo" class="retrieve-button">Retrieve Stats</button>
      </div>
    </div>

    <div class="selected-range">
      Selected Range: {{ beginningMonth }}/{{ beginningYear }} to {{ endMonth }}/{{ endYear }}
    </div>

    <!-- Display retrieved information -->
    <div v-if="showInfo" class="retrieved-info">
      <h3>New Users Stats:</h3>
      <p>Number of New Users: {{ newUsersStats.numberOfNewUsers }}</p>
      <p>Number of Contributions by them: {{ newUsersStats.numberOfContributions }}</p>
      <p>Total Value of Contributions by them: {{ newUsersStats.totalValueOfContributions }}</p>
      <p>Average Value of Contributions by them: {{ newUsersStats.averageValueOfContributions }}</p>

      <h3>Projects Stats:</h3>
      <p>Number of Created Projects: {{ projectsStats.numberOfCreatedProjects }}</p>
      <p>Number of Finished Projects: {{ projectsStats.numberOfFinishedProjects }}</p>
      <p>Number of Contributions: {{ projectsStats.numberOfContributions }}</p>

      <h3>New Collaborators Stats (taken into account only contributions by them in newly joined projects):</h3>
      <p>Number of New Collaborators: {{ newCollaboratorsStats.numberOfNewUsers }}</p>
      <p>Number of Contributions by them: {{ newCollaboratorsStats.numberOfContributions }}</p>
      <p>Total Value of Contributions by them: {{ newCollaboratorsStats.totalValueOfContributions }}</p>
      <p>Average Value of Contributions by them: {{ newCollaboratorsStats.averageValueOfContributions }}</p>
    </div>

  </div>
</template>

<script>

import AdminNavigation from "@/components/AdminNavigation";
import {getStats} from "@/services/adminService";

export default {
  name: "AdminPage",
  components: {AdminNavigation},
  data() {
    return {
      showInfo: false,
      newUsersStats: {},
      projectsStats: {},
      newCollaboratorsStats: {},
      months: Array.from({ length: 12 }, (_, i) => ({
        value: i + 1,
        label: new Date(0, i).toLocaleString('en-US', { month: 'long' })
      })),
      years: [],
      beginningMonth: new Date().getMonth() + 1, // Default start month
      beginningYear: new Date().getFullYear(), // Default start year
      endMonth: new Date().getMonth() + 1, // Default end month
      endYear: new Date().getFullYear() // Default end year
    };
  },
  mounted() {
    const currentYear = new Date().getFullYear();
    for (let year = currentYear; year >= currentYear - 10; year--) {
      this.years.push(year);
    }

    this.retrieveInfo();
  },
  methods: {
    retrieveInfo() {
        getStats(this.beginningMonth, this.beginningYear, this.endMonth, this.endYear).then((response) => {
          if (response) {
            const stats = response;
            this.newUsersStats = stats.newUsersStats
            this.newUsersStats.averageValueOfContributions = this.newUsersStats.totalValueOfContributions / this.newUsersStats.numberOfContributions
            this.projectsStats = stats.projectsStats
            this.newCollaboratorsStats = stats.newCollaboratorsStats
            this.newCollaboratorsStats.averageValueOfContributions = this.newCollaboratorsStats.totalValueOfContributions / this.newCollaboratorsStats.numberOfContributions
            this.showInfo = true;
          }
        })
    }
  }
};
</script>

<style>
.date-range-container {
  font-family: Arial, sans-serif;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.date-selectors {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.date-select {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
}

.selected-range {
  margin-top: 20px;
  font-weight: bold;
}
</style>