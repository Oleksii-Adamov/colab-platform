<template>
  <UserNavigation></UserNavigation>
  <ProfileNavigation></ProfileNavigation>
  <template v-if="user !== null && user !== undefined">
   <h1>{{this.user.fullName}}</h1>
    <h2 v-if="this.project.numberOfRatings && this.project.numberOfRatings > 0">User's rating (average from projects): {{this.user.rating}}/5 (based on {{this.user.numberOfRatings}} grades)</h2>
    <h2 v-else>This user doesn't yet have a rating</h2>
    <h2>Bio:</h2>
    <div>{{this.user.bio}}</div>
    <h2>Interests:</h2>
    <div v-for="tag in this.user.tags" :key="tag.id">
      <div>{{tag.name}}</div>
    </div>
    <h2>Skills:</h2>
    <div v-for="skill in this.user.skills" :key="skill.id">
      <div>{{skill.name}}</div>
    </div>
    <button v-if="is_owner === true" @click="editProfile" style="font-size: 24px">Edit profile</button>
    <h2>Ongoing projects:</h2>
    <div class="custom-table">
      <table>
        <thead>
        <tr>
          <th>Project</th>
          <th>Role</th>
          <th>Total value</th>
          <th>Total contributions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="unfinishedProjectUserInfo in unfinishedProjectsUserInfo" :key="unfinishedProjectUserInfo.project.id">
          <td>{{ unfinishedProjectUserInfo.project.name }}</td>
          <td>{{ unfinishedProjectUserInfo.role }}</td>
          <td>{{ unfinishedProjectUserInfo.value}}</td>
          <td>{{ unfinishedProjectUserInfo.contributions }}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <h2>Finished projects:</h2>
  </template>
</template>

<script>
import ProfileNavigation from "@/components/ProfileNavigation";
export default {
  name: "ProfilePage",
  components: {ProfileNavigation}
}
</script>

<style scoped>

</style>