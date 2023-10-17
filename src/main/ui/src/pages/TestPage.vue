<template>
  <div>
    <h2>From server</h2>
    <ul>
      <li v-for="item in items" :key="item.id">
        <div>{{item.name}}</div>
      </li>
    </ul>
  </div>
</template>

<script>
import {getTestItems} from "@/services/testService";

export default {
  name: "TestPage",
  data() {
    return {
      items: []
    }
  },
  methods: {
    getTestItems() {
      getTestItems().then(response => {
        console.log(response);
        this.items = response;
      })
    },
  },
  mounted() {
    this.getTestItems();
  }
}
</script>

<style scoped>

</style>