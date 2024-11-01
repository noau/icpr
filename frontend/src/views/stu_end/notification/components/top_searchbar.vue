<template>
  <div class="search-bar">
    <el-input
        placeholder="搜索"
        :value="localSearchText"
    @input="updateLocalSearchText"
    @keyup.enter="submitSearch"
    class="search-input"
    />
    <el-select
        :value="sortOption"
    @change="updateSortOption"
    placeholder="排序方式"
    class="sort-select"
    >
    <el-option label="日期" value="date"></el-option>
    <el-option label="标题" value="title"></el-option>
    </el-select>
  </div>
</template>

<script>
export default {
  props: {
    searchText: {
      type: String,
      default: ''
    },
    sortOption: {
      type: String,
      default: 'date'
    }
  },
  data() {
    return {
      localSearchText: this.searchText // 使用本地状态存储搜索文本
    };
  },
  watch: {
    searchText(newVal) {
      this.localSearchText = newVal; // 监听父组件的 searchText
    },
    localSearchText(newVal) {
      this.$emit('update:searchText', newVal); // 同步到父组件
    }
  },
  methods: {
    updateLocalSearchText(value) {
      this.localSearchText = value; // 更新本地搜索文本
    },
    submitSearch() {
      console.log(`搜索文本: ${this.localSearchText}`); // 输出当前搜索文本
      if (!this.localSearchText) {
        this.$emit('update:searchText', ''); // 保持为空以显示所有通知
      } else {
        this.$emit('update:searchText', this.localSearchText); // 保持当前输入文本
      }
    },
    updateSortOption(value) {
      this.$emit('update:sortOption', value);
    }
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px 0;
}

.search-input {
  flex: 1;
}

.sort-select {
  width: 150px;
}
</style>
