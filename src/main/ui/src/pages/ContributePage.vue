<template>

</template>

<script>
import UserNavigation from "@/components/UserNavigation";
import {createProject} from "@/services/ProjectService";
import {getTags} from "@/services/tagService";
import {getSkills} from "@/services/skillService";

export default {
  name: "ContributePage",
  components: {
    UserNavigation
  },
  data() {
    return {
      contributionDescription: "",
      contributionValue: 0,
    }
  },
  methods: {
    submitContribution() {
      if (this.contributionDescription !== "") {
        createProject(this.projectName, this.projectDescription, this.selectedTags, this.selectedSkills, () => {
          this.$router.push({path: '/my-projects'});
        });
      } else {
        alert("All field must be field");
      }
    },
    getPossibleTagsAndSkills() {
      getTags().then(response => {
        console.log(response)
        this.tags = response
      })
      getSkills().then(response => {
        console.log(response)
        this.skills = response
      })
    }
  },
  mounted() {
    this.getPossibleTagsAndSkills();
  }
}
</script>

<style scoped>

</style>