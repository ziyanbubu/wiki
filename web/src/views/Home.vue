<template>
  <a-layout :style="{display: 'flex'}">
    <a-layout-sider width="200" style="background: #fff">
    <a-menu
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-sub-menu key="sub1">
        <template #title>
                <span>
                  <user-outlined />
                  subnav 1
                </span>
        </template>
        <a-menu-item key="1">option1</a-menu-item>
        <a-menu-item key="2">option2</a-menu-item>
        <a-menu-item key="3">option3</a-menu-item>
        <a-menu-item key="4">option4</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub2">
        <template #title>
                <span>
                  <laptop-outlined />
                  subnav 2
                </span>
        </template>
        <a-menu-item key="5">option5</a-menu-item>
        <a-menu-item key="6">option6</a-menu-item>
        <a-menu-item key="7">option7</a-menu-item>
        <a-menu-item key="8">option8</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub3">
        <template #title>
                <span>
                  <notification-outlined />
                  subnav 3
                </span>
        </template>
        <a-menu-item key="9">option9</a-menu-item>
        <a-menu-item key="10">option10</a-menu-item>
        <a-menu-item key="11">option11</a-menu-item>
        <a-menu-item key="12">option12</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
    <a-layout style="padding: 0 24px 24px; flex: 1">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <pre>
{{ ebook }}
{{ ebooks2 }}
        </pre>

      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from "vue";
import axios from 'axios';

export default defineComponent({
  name: 'Home',
  setup(){
    console.log("setup");
    const ebook = ref();
    const ebook1 = reactive({books: []});
    onMounted(() => {
      console.log("onMounted");
      axios.get("http://localhost:8880/ebook/list?name=Vue").then((response) => {
        const data = response.data;
        ebook.value = data.content
        ebook1.books = data.content
        console.log(response);
      });
    })

    return {
      ebook,
      ebooks2: toRef(ebook1, "books")
    }

  }
})

</script>
