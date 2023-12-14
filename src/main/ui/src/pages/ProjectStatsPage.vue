<template>
  <UserNavigation></UserNavigation>
  <ProjectAdminNavigation></ProjectAdminNavigation>
  <div class="date-range-container">
    <div class="date-selectors">
      <div class="date-select">
        <label for="startMonth">Start Month:</label>
        <select v-model="beginningMonth" @change="updateData">
          <option v-for="month in months" :key="month.value" :value="month.value">{{ month.label }}</option>
        </select>
      </div>

      <div class="date-select">
        <label for="startYear">Start Year:</label>
        <select v-model="beginningYear" @change="updateData">
          <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
        </select>
      </div>

      <div class="date-select">
        <label for="endMonth">End Month:</label>
        <select v-model="endMonth" @change="updateData">
          <option v-for="month in months" :key="month.value" :value="month.value">{{ month.label }}</option>
        </select>
      </div>

      <div class="date-select">
        <label for="endYear">End Year:</label>
        <select v-model="endYear" @change="updateData">
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
      <h3>Project Contributions Stats:</h3>
      <p>Number of Contributions: {{ contributionsStats.numberOfContributions }}</p>
      <p>Total Value of Contributions: {{ contributionsStats.totalValueOfContributions }}</p>
      <p>Average Value of Contributions: {{ contributionsStats.averageValueOfContributions }}</p>

      <h3>Project New Collaborators Stats:</h3>
      <p>Number of New Collaborators: {{ newUsersStats.numberOfNewUsers }}</p>
      <p>Number of Contributions by them: {{ newUsersStats.numberOfContributions }}</p>
      <p>Total Value of Contributions by them: {{ newUsersStats.totalValueOfContributions }}</p>
      <p>Average Value of Contributions by them: {{ newUsersStats.averageValueOfContributions }}</p>
    </div>

  </div>
</template>

<script>
import UserNavigation from "@/components/UserNavigation";
import ProjectAdminNavigation from "@/components/ProjectAdminNavigation";
import {getProjectStats} from "@/services/ProjectService";

export default {
  name: "ProjectStatsPage",
  components: {UserNavigation, ProjectAdminNavigation},
  data() {
    return {
      showInfo: false,
      contributionsStats: {},
      newUsersStats: {},
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
      const projectId = localStorage.getItem('projectId')
      if (projectId) {
        getProjectStats(projectId, this.beginningMonth, this.beginningYear, this.endMonth, this.endYear).then((response) => {
          if (response) {
            const stats = response;
            this.contributionsStats = stats.contributionsStats
            this.contributionsStats.averageValueOfContributions = this.contributionsStats.totalValueOfContributions / this.contributionsStats.numberOfContributions
            this.newUsersStats = stats.newUsersStats
            this.newUsersStats.averageValueOfContributions = this.newUsersStats.totalValueOfContributions / this.newUsersStats.numberOfContributions;
            this.showInfo = true;
          }
        })
        // Perform data retrieval based on the selected range
        // Replace this section with your actual data retrieval logic
        // For demo purposes, setting mock data
        // this.contributionsStats = {
        //   numberOfContributions: 150,
        //   totalValueOfContributions: 5000,
        //   averageValueOfContributions: 33.33
        // };
        // this.newUsersStats = {
        //   numberOfNewUsers: 20,
        //   numberOfContributions: 70,
        //   totalValueOfContributions: 2000,
        //   averageValueOfContributions: 28.57
        // };
        //
        // this.showInfo = true;
      } else {
        this.showInfo = false;
        alert("Project session expired")
        this.$router.push({path: '/my-projects'});
      }
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
