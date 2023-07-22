<template>
  <a-layout :style="{display: 'flex'}">
    <a-layout-sider width="200" style="background: #fff">
    <a-menu
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
        @click="handleClick"
    >
      <a-menu-item key="welcome">
          <HomeOutlined />
          <span>欢迎</span>
        </a-menu-item>
      <a-sub-menu v-for="item in level1" :key="item.id">
        <template v-slot:title>
          <span><BarsOutlined />{{item.name}}</span>
        </template>
        <a-menu-item v-for="child in item.children" :key="child.id">
          📖 &nbsp; <span>{{child.name}}</span>
        </a-menu-item>
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
        <div class="welcome" v-show="isShowWelcome">
          <hi>欢迎</hi>
        </div>
        <a-list  v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3}" :data-source="ebook">

          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component v-bind:is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
              </template>

              <a-list-item-meta :description="item.description">
                <template #title>
                  <router-link :to="'/doc?ebookId=' + item.id">
                    {{ item.name }}
                  </router-link>
                </template>
                <template #avatar><a-avatar :src="item.cover" /></template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>

      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from "vue";
import axios from 'axios';
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";
import _default from "ant-design-vue/es/color-picker";
// const listData: Record<string, string>[] = [];
//
// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: 'https://www.antdv.com/',
//     title: `ant design vue part ${i}`,
//     avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
//     description:
//         'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//     content:
//         'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//   });
// }


export default defineComponent({
  name: 'Home',
  computed: {
    _default() {
      return _default
    }
  },
  setup(){
    const ebook = ref();
    const isShowWelcome = ref(true);
    let categoryId2 = 0;

    const level1 = ref(); // 一级分类树，children属性就是二级分类
    let categorys: any;
    /**
     * 查询所有分类
     *
     */
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };

    const handleQueryEbook = () => {
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2
        }

      }).then((response) => {
        const data = response.data;
        ebook.value = data.content.list;
        // ebooks1.books = data.content;
      });
    }

    const handleClick = (value: any) => {
      // console.log("menu click", value);
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        isShowWelcome.value = false;
        categoryId2 = value.key;
        handleQueryEbook();
      }
    };


    onMounted(() => {
      handleQueryCategory();

    })
    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];
    return {
      ebook,
      pagination,
      actions,
      handleClick,
      level1,
      isShowWelcome,
    };

  }
})

</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>