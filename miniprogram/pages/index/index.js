//index.js
const app = getApp()
var myUtils = require("../../utils/myUtil.js")
Page({
  data: {
    longitude: 0 ,
    latitude: 0 ,
    controls: [] ,
    markers: []
  },

  //首次加载页面时调用
  onLoad: function() {
    var that = this;
    wx.getLocation({
      success: function(res) {
        var longitude = res.longitude
        var latitude = res.latitude
        
        that.setData({
            longitude:longitude,
            latitude:latitude
        })
        findBikes(longitude, latitude, that);
      },
    });

    wx.getSystemInfo({
      success: function(res) {
        var windowWidth = res.windowWidth;
        var windowHeight = res.windowHeight;
        that.setData({
            controls: [
              { //扫码
                id: 1,
                iconPath: '/images/open.png',
                position: {
                  width: windowWidth/2,
                  height: 60,
                  left: windowWidth / 2 -100 ,
                  top: windowHeight - 70
                },
              clickable:true
              },
              { //定位
                id:2,
                iconPath: '/images/img1.png',
                position: {
                  width: 40,
                  height: 40,
                  left: 10,
                  top: windowHeight - 60
                 },
                clickable: true
              }, 
              { //位置指针
                id: 3,
                iconPath: '/images/location.png',
                position: {
                  width: 20,
                  height: 35,
                  left: windowWidth / 2 - 10,
                  top: windowHeight / 2 - 40
                },
                clickable: true
              }, 
              {
                // 充值按钮
                id: 4,
                iconPath: '/images/pay.png',
                position: {
                  width: 40,
                  height: 40,
                  left: windowWidth - 45,
                  top: windowHeight - 100
                },
                clickable: true
              },
              {
                // 报修
                id: 5,
                iconPath: "/images/warn.png",
                position: {
                  width: 35,
                  height: 35,
                  left: windowWidth - 42,
                  top: windowHeight - 60
                },
                clickable: true
              },
              { //添加车辆
                id: 6,
                iconPath: '/images/add.png',
                position: {
                  width: 35,
                  height: 35
                },
                clickable: true
              }                                          
            ]
          })
      },
    })
  },
  controltap:function(e){
    var that = this
    var cid = e.controlId;
    switch(cid){
      //注册
      case 1:{
        var status = myUtils.get("status")
        //console.log(status)
        if(status == 0){
          wx.navigateTo({
           url: '../register/register',
          })
        }else if(status == 1){
          wx.navigateTo({
            url: '../deposit/deposit',
          })
        } else if (status == 2) {
          wx.navigateTo({
            url: '../identify/identify',
          })
        }
        break
      }
      case 2:{
        this.mapCtx.moveToLocation()
        break;
      }
      case 4:{  
        var status = myUtils.get("status")
        //console.log(status)
        if (status == 0) {
          wx.navigateTo({
            url: '../register/register',
          })
        } else if (status == 1) {
          wx.navigateTo({
            url: '../deposit/deposit',
          })
        } else if (status == 2) {
          wx.navigateTo({
            url: '../identify/identify',
          })
        } else if (status == 3) {
          wx.navigateTo({
            url: '../pay/pay',
          })
        }
        break
      }
      case 6:{
          this.mapCtx.getCenterLocation({
          success: function (res) {
            var log = res.longitude;
            var lat = res.latitude;    
        wx.request({
          url:"http://localhost:8000/bike/addBike",
          data:{
            status:0,
            location: [log, lat],
          },
          method:'POST',
          success:function(res){
            findBikes(log, lat, that);
            console.log(res)
          }
        })
       }
     })
     break;      
    }
   }
  },
  onReady: function () {
    this.mapCtx = wx.createMapContext('myMap')
  },
  regionchange: function (e) {
    var that = this;
    // 获取移动后的位置
    var etype = e.type;
    if (e.type == 'end') {
      this.mapCtx.getCenterLocation({
        success: function (res) {
          var longitude = res.longitude;
          var latitude = res.latitude;
          findBikes(longitude, latitude, that);
     }  
  })
    }
  }
})
// 自定义方法
function findBikes(log, lat, that) {
  wx.request({
    url: 'http://localhost:8000/bike/findNear',
    method: 'GET',
    data: {
      longitude: log,
      latitude: lat
    },
    success: function (res) {
      console.log("bike",res)
      var bikes = res.data.map((geoResult) => {
        return {
          longitude: geoResult.content.location[0],
          latitude: geoResult.content.location[1],
          iconPath: '/images/bike.png',
          width: 35,
          height: 40,
          id: geoResult.content.id
        }
      })

      // 将bike数组set到当前页面的marker
      that.setData({
        markers: bikes
      })

    }
  })
}