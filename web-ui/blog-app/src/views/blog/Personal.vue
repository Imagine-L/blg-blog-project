<template>
  <div class="me-allct-body" v-title :data-title="personalTitle">
    <el-container class="me-allct-container">
      <el-main>
        <el-tabs v-model="activeName">
          <el-tab-pane label="个人信息" name="personInfo">
            <personal-info></personal-info>
<!--            <test-info></test-info>-->
          </el-tab-pane>

          <el-tab-pane label="修改密码" name="changePwd" @change="console.log('hello')">
            <change-password></change-password>
          </el-tab-pane>

          <el-tab-pane label="我的文章" name="articleTab">
            <el-radio-group v-model="sortValue" size="mini" @change="getArticles">
              <el-radio-button label="最新发布"></el-radio-button>
              <el-radio-button label="最多访问"></el-radio-button>
              <el-radio-button label="最多评论"></el-radio-button>
            </el-radio-group>
            <article-item v-for="a in pageArticle.articles" :key="a.id" v-bind="a" class="article-item"></article-item>
            <div class="block">
              <el-pagination
                @current-change="changeArticleCurrentPage"
                :current-page.sync="pageArticle.currPage"
                :page-size="pageArticle.pageSize"
                layout="prev, pager, next, jumper"
                :total="pageArticle.total">
              </el-pagination>
            </div>
          </el-tab-pane>

          <el-tab-pane label="我的评论" name="commentTab">
             <personal-comment-item
              v-for="(c,index) in pageComment.comments"
              :comment="c"
              :articleId="c.articleId"
              :index="index"
              :rootCommentCounts="pageComment.comments.length"
              :key="c.id">
            </personal-comment-item>
            <div class="block">
              <el-pagination
                @current-change="changeCommentCurrentPage"
                :current-page.sync="pageComment.currPage"
                :page-size="pageComment.pageSize"
                layout="prev, pager, next, jumper"
                :total="pageComment.total">
              </el-pagination>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import {getCommentByAuthor} from "@/api/comment";
import defaultAvatar from '@/assets/img/logo.png'
import ArticleItem from "@/components/article/ArticleItem";
import {getArticlesByAuthor} from "@/api/article";
import PersonalCommentItem from "../../components/comment/PersonalCommentItem";
import ChangePassword from "../../components/personal/ChangePassword";
import PersonalInfo from "../../components/personal/PersonalInfo";
// import CommentItem from "../../components/comment/CommentItem";
// import ArticleScrollPage from "../common/ArticleScrollPage";


export default {
  name: 'Personal',
  created() {
    this.getArticles()
    this.getComments()
  },
  components: {
    ArticleItem,
    PersonalCommentItem,
    ChangePassword,
    PersonalInfo
  },
  data() {
    return {
      defaultAvatar: defaultAvatar,
      showCategoryTab: false,
      showTagTab: false,
      categorys: [],
      tags: [],
      currentActiveName: 'category',
      sortValue: '最新发布',
      articles: [],
      pageArticle: {
        total: 1000,
        pageSize: 10,
        currPage: 1,
        articles: []
      },
      pageComment: {
        total: 1000,
        pageSize: 10,
        currPage: 1,
        comments: []
      }
    }
  },
  computed: {
    activeName: {
      get() {
        return 'personInfo'
      },
      set(newValue) {
        this.currentActiveName = newValue
      }
    },
    personalTitle() {
      // if (this.currentActiveName === 'articleTab') {
      //   return '个人中心 - 博乐阁'
      // }
      // return '标签 - 博乐阁'
      return '个人中心 - 博乐阁'
    },
    sortWord() {
      let sortWord = ''
      if (this.sortValue === '最多评论') {
        sortWord = 'commentCounts'
      } else if (this.sortValue === '最多访问') {
        sortWord = 'viewCounts'
      } else {
        sortWord = 'createDate'
      }
      return sortWord;
    }
  },
  methods: {
    view(id) {
      this.$router.push({path: `/${this.currentActiveName}/${id}`})
    },
    getArticles() {
      getArticlesByAuthor(this.pageArticle.currPage, this.pageArticle.pageSize, this.sortWord, this.$store.state.token).then(resp => {
        if (resp.success) {
          this.pageArticle.total = resp.data.total
          this.pageArticle.pageSize = resp.data.pageSize
          this.pageArticle.articles = resp.data.articles
        }
      })
    },
    getComments() {
      getCommentByAuthor(this.pageComment.currPage, this.pageComment.pageSize, this.$store.state.token).then(resp => {
        if (resp.success) {
          this.pageComment.total = resp.data.total
          this.pageComment.pageSize = resp.data.pageSize
          this.pageComment.comments = resp.data.comments
        }
      })
    },
    changeArticleCurrentPage(val) {
      this.getArticles()
    },
    changeCommentCurrentPage(val) {
      this.getComments()
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

.el-radio-group {
  margin-bottom: 10px;
}

.article-item {
  margin: 10px 0;
}
</style>
