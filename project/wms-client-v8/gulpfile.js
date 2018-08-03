var gulp = require('gulp');
var bom = require("gulp-bom");
var uglify = require('gulp-uglify');
var minifyCss = require('gulp-minify-css');
var minifyHtml = require('gulp-minify-html');
var htmlMin = require("gulp-htmlmin");
var browserSync = require('browser-sync'), reload = browserSync.reload;
var del = require('del');
var ngAnnotate = require("gulp-ng-annotate");

// 监听
gulp.task('serve', function(){
  browserSync({
    notify: false,
    port: 9191,
    server: {
      baseDir: ['app'],
      routes: {
        '/bower_components': 'bower_components'
      }
    }
  });

  gulp.watch([
    '!app/bower_components/**',
    'app/*.html',
    'app/*.js',
    'app/css/*.css',
    'app/**/*.html',
    'app/**/*.js'
  ]).on('change', reload);
});

gulp.task('serve:dist', function(){
  browserSync({
    notify: false,
    port: 9191,
    server: {
      baseDir: ['app']
    }
  });
});

// 压缩css
gulp.task('minifyCss', function(){
  return  gulp.src('app/css/*.css')
    .pipe(bom())
    .pipe(minifyCss({
      advanced: false, // 默认true,是否开启高级优化(合并选择器等)
      compatibility: "ie7", // 保留ie7及以下兼容写法
      keepSpecialComments: "*" // 保留所有特殊前缀
    }))
    .pipe(gulp.dest('dist/css'));
});

// 输出image
gulp.task('image', function(){
  return gulp.src(['!app/bower_components/**', 'app/image/**'])
    .pipe(gulp.dest('dist/image'));
});

// 压缩js
gulp.task('uglify', function(){
  return gulp.src(['!app/bower_components/**', 'app/*.js', 'app/**/*.js'])
    .pipe(bom())
    .pipe(ngAnnotate())
    .pipe(uglify())
    .pipe(gulp.dest('dist'));
});

// 输出json
gulp.task('json', function(){
  return gulp.src(['!app/bower_components/**', 'app/**/*.json'])
    .pipe(gulp.dest('dist'));
});

// 压缩html
gulp.task('minifyHtml', function(){
  return gulp.src(['!app/bower_components/**', 'app/index.html', 'app/**/*.html'])
    .pipe(bom())
    .pipe(htmlMin({
      removeComments: true, // 清除HTML注释
      collapseWhitespace: false,// 压缩HTML
      collapseBooleanAttributes: false, // 省略布尔属性的值 <input checked="true"/> ==> <input />
      removeEmptyAttributes: true, // 删除所有空格作属性值 <input id="" /> ==> <input />
      removeScriptTypeAttributes: true, // 删除<script>的type="text/javascript"
      removeStyleLinkTypeAttributes: true, // 删除<style>和<link>的type="text/css"
      minifyJS: true, // 压缩页面JS
      minifyCSS: true // 压缩页面CSS
    }))
    .pipe(gulp.dest('dist'));
});

// others
gulp.task('others', function(){
  return gulp.src("app/bower_components/**")
    .pipe(gulp.dest('dist/bower_components'));
});

// 清除
gulp.task('clean', function(){
  return del(['dist']);
});

// 打包
gulp.task('package', ['clean'], function(){
  gulp.start(['minifyCss', 'image', 'uglify', 'minifyHtml', 'others']);
});

// 默认
gulp.task('default', function(){
  console.log("this is default command");
});