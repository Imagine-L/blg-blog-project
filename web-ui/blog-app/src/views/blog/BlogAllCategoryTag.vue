<template>
  <div class="me-allct-body" v-title :data-title="categoryTagTitle">
    <el-container class="me-allct-container">
      <el-main>
        <el-tabs v-model="activeName">
          <el-tab-pane label="文章分类" name="category">
            <el-button
              type="primary"
              @click="isShowTab"
              plain
              round
            >添加分类
            </el-button>
            <el-button
              type="danger"
              @click="doDeleteEmptyCategory"
              plain
              round
            >删除空分类
            </el-button>
            <ul class="me-allct-items">
              <li v-for="c in categorys" @click="view(c.id)" :key="c.id" class="me-allct-item">
                <div class="me-allct-content">
                  <a class="me-allct-info">
                    <img class="me-allct-img" :src="c.avatar?c.avatar:defaultAvatar"/>
                    <h4 class="me-allct-name">{{ c.categoryName }}</h4>
                    <p class="me-allct-description">{{ c.description }}</p>
                  </a>

                  <div class="me-allct-meta">
                    <span>{{ c.articles }} 文章</span>
                  </div>
                </div>
              </li>
            </ul>
            <el-dialog
              title="提示"
              :visible.sync="showCategoryTab"
              width="30%"
            ><!-- :before-close="" -->
              <el-form ref="uploadCategory" :model="uploadCategory" label-width="80px">
                <el-form-item label="分类名称">
                  <el-input v-model="uploadCategory.categoryName"></el-input>
                </el-form-item>
                <el-form-item label="分类图像">
                  <input type="file" ref="uploadImage" :accept="imgAccept" @change="setCategoryFormData"></input>
                </el-form-item>
                <el-form-item label="分类描述">
                  <el-input v-model="uploadCategory.description"></el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="showCategoryTab = false">取 消</el-button>
                <el-button type="primary" @click="doSaveCategory">确 定</el-button>
              </span>
            </el-dialog>
          </el-tab-pane>

          <el-tab-pane label="标签" name="tag">
            <el-button
              type="primary"
              @click="isShowTab"
              plain
              round>添加标签
            </el-button>
            <el-button
              type="danger"
              @click="deleteEmptyTag"
              plain
              round
            >删除空标签
            </el-button>
            <ul class="me-allct-items">
              <li v-for="t in tags" @click="view(t.id)" :key="t.id" class="me-allct-item">
                <div class="me-allct-content">
                  <a class="me-allct-info">
                    <img class="me-allct-img" :src="t.avatar?t.avatar:defaultAvatar"/>
                    <h4 class="me-allct-name">{{ t.tagName }}</h4>
                  </a>

                  <div class="me-allct-meta">
                    <span>{{ t.articles }}  文章</span>
                  </div>
                </div>
              </li>
            </ul>
            <el-dialog
              title="提示"
              :visible.sync="showTagTab"
              width="30%">
              <el-form ref="uploadCategory" :model="uploadTag" label-width="80px">
                <el-form-item label="标签名称">
                  <el-input v-model="uploadTag.tagName"></el-input>
                </el-form-item>
                <el-form-item label="标签图像">
                  <input type="file" ref="uploadImage" :accept="imgAccept" @change="setTagFormData"></input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button @click="showTagTab = false">取 消</el-button>
                <el-button type="primary" @click="doSaveTag">确 定</el-button>
              </span>
            </el-dialog>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import defaultAvatar from '@/assets/img/logo.png'
import {getAllCategorysDetail, saveCategory, deleteEmptyCategory} from '@/api/category'
import {getAllTagsDetail, saveTag, deleteEmptyTag} from '@/api/tag'
import {upload} from '@/api/upload'
import {getToken} from "@/request/token";

export default {
  name: 'BlogAllCategoryTag',
  created() {
    this.getCategorys()
    this.getTags()
  },
  data() {
    return {
      imgAccept: '.jpg, .png, .gif, .jpeg, .svg, .ico',
      defaultAvatar: defaultAvatar,
      showCategoryTab: false,
      showTagTab: false,
      categorys: [],
      tags: [],
      currentActiveName: 'category',
      uploadCategory: {
        categoryName: '',
        avatar: '',
        description: '',
      },
      uploadTag: {
        tagName: '',
        avatar: ''
      },
      categoryFormData: new FormData(),
      tagFormData: new FormData()
    }
  },
  computed: {
    activeName: {
      get() {
        return (this.currentActiveName = this.$route.params.type)
      },
      set(newValue) {
        this.currentActiveName = newValue
      }
    },
    categoryTagTitle() {
      if (this.currentActiveName === 'category') {
        return '文章分类 - 博乐阁'
      }
      return '标签 - 博乐阁'
    }
  },
  methods: {
    view(id) {
      this.$router.push({path: `/${this.currentActiveName}/${id}`})
    },
    getCategorys() {
      let that = this
      getAllCategorysDetail().then(data => {
        that.categorys = data.data
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '文章分类加载失败', showClose: true})
        }
      })
    },
    getTags() {
      let that = this
      getAllTagsDetail().then(data => {
        that.tags = data.data
      }).catch(error => {
        if (error !== 'error') {
          that.$message({type: 'error', message: '标签加载失败', showClose: true})
        }
      })
    },
    doSaveCategory() {
      // 合法性校验
      if (this.uploadCategory.description === '' ||
        this.uploadCategory.categoryName === '' ||
        this.categoryFormData.get("image") === null) {
        this.$message.info({message: '请输入分类信息后再次确认'})
        return
      }
      this.showCategoryTab = false
      // 上传图片
      upload(this.categoryFormData, this.$store.state.token).then(resp => {
        return new Promise((resolve, reject) => {
          resolve(resp.data);
        })
      }).then(data => {
        // 上传分类数据
        this.uploadCategory.avatar = data
        saveCategory(this.uploadCategory, this.$store.state.token).then(resp => {
          if (resp.success) {
            this.categorys.push(resp.data)
          } else {
            this.$message.error({message: '添加分类失败，请稍后重试'})
          }
        })
      }).catch(err => {
        this.$message.error({message: '添加分类失败，请稍后重试'})
      })
    },
    doSaveTag() {
      // 合法性校验
      if (this.uploadTag.tagName === '' ||
        this.tagFormData.get("image") === null) {
        this.$message.info({message: '请输入分类信息后再次确认'})
        return
      }
      this.showTagTab = false
      // 上传图片
      upload(this.tagFormData, this.$store.state.token).then(resp => {
        return new Promise((resolve, reject) => {
          resolve(resp.data);
        })
      }).then(data => {
        // 上传标签数据
        this.uploadTag.avatar = data
        saveTag(this.uploadTag, this.$store.state.token).then(resp => {
          if (resp.success) {
            this.tags.push(resp.data)
          } else {
            this.$message.error({message: '添加标签失败，请稍后重试'})
          }
        })
      }).catch(err => {
        this.$message.error({message: '添加标签失败，请稍后重试'})
      })
    },
    setCategoryFormData(event) {
      this.categoryFormData = new FormData();
      this.categoryFormData.append("image", event.target.files[0])
    },
    setTagFormData(event) {
      this.tagFormData = new FormData();
      this.tagFormData.append("image", event.target.files[0])
    },
    doDeleteEmptyCategory() {
      if (!this.checkLogin()) {
        this.$message({type: 'warning', message: '请先登录哦', showClose: true})
        return
      }
      this.$confirm('确认删除？').then(_ => {
        deleteEmptyCategory(this.$store.state.token).then(resp => {
          if (resp.success) {
            let unUseCategoryIds = resp.data
            this.categorys = this.categorys.filter(category => !unUseCategoryIds.includes(category.id + ''))
          } else {
            this.$message.error({message: '删除分类失败，请稍后重试', showClose: true})
          }
        })
      }).catch(_ => {
        this.$message.warning({message: '取消删除分类', showClose: true})
      })
    },
    deleteEmptyTag() {
      if (!this.checkLogin()) {
        this.$message({type: 'warning', message: '请先登录哦', showClose: true})
        return
      }
      this.$confirm('确认删除？').then(_ => {
        deleteEmptyTag(this.$store.state.token).then(resp => {
          if (resp.success) {
            let unUseTagIds = resp.data
            this.tags = this.tags.filter(tag => !unUseTagIds.includes(tag.id + ''))
          } else {
            this.$message.error({message: '删除标签失败，请稍后重试', showClose: true})
          }
        })
      }).catch(_ => {
        this.$message.warning({message: '取消删除标签', showClose: true})
      })
    },
    // 判断是否登录
    checkLogin() {
      let token = getToken();

      return token !== undefined && token !== '';
    },
    isShowTab() {
      if (this.checkLogin()) {
        if (this.currentActiveName === 'category') {
          this.showCategoryTab = !this.showCategoryTab
        } else {
          this.showTagTab = !this.showTagTab
        }
      } else {
        this.$message({type: 'warning', message: '请先登录哦', showClose: true})
      }
    }
  },
  //组件内的守卫 调整body的背景色
  beforeRouteEnter(to, from, next) {
    window.document.body.style.backgroundColor = '#fff';
    next();
  },
  beforeRouteLeave(to, from, next) {
    window.document.body.style.backgroundColor = '#f5f5f5';
    next();
  }
}
</script>

<style>
.me-allct-body {
  margin: 60px auto 140px;
}

.me-allct-container {
  width: 1000px;
}

.me-allct-items {
  padding-top: 2rem;
}

.me-allct-item {
  width: 25%;
  display: inline-block;
  margin-bottom: 2.4rem;
  padding: 0 .7rem;
  box-sizing: border-box;
}

.me-allct-content {
  display: inline-block;
  width: 100%;
  background-color: #fff;
  border: 1px solid #f1f1f1;
  transition: border-color .3s;
  text-align: center;
  padding: 1.5rem 0;
}

.me-allct-info {
  cursor: pointer;
}

.me-allct-img {
  margin: -40px 0 10px;
  width: 60px;
  height: 60px;
  vertical-align: middle;

}

.me-allct-name {
  font-size: 21px;
  font-weight: 150;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 4px;
}

.me-allct-description {
  min-height: 50px;
  font-size: 13px;
  line-height: 25px;
}

.me-allct-meta {
  font-size: 12px;
  color: #969696;
}
</style>
