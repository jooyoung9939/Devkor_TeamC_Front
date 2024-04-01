package com.example.deckor_teamc_front

import android.os.Bundle
import android.text.Layout
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class MainActivity : FragmentActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)


    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        val marker1 = Marker()
        marker1.position = LatLng(37.586868,127.0313414)
        marker1.map = naverMap
        marker1.icon = OverlayImage.fromResource(R.drawable.spot)

        val marker2 = Marker()
        marker2.position = LatLng(37.5843837,127.0274333)
        marker2.map = naverMap
        marker2.icon = OverlayImage.fromResource(R.drawable.spot)

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5871834,127.0298899))
            .animate(CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)


        //버튼 모달
        val bottomSheet1 = findViewById<ConstraintLayout>(R.id.bottomsheet1)
        //val bottomSheet2 = findViewById<ConstraintLayout>(R.id.bottomsheet2)

        var isBottomSheetVisible = false

        marker1.setOnClickListener {
            if (isBottomSheetVisible) {
                val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
                bottomSheet1.startAnimation(slideDown)
                bottomSheet1.visibility = View.GONE
                isBottomSheetVisible = false
            } else {
                val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                bottomSheet1.startAnimation(slideUp)
                bottomSheet1.visibility = View.VISIBLE
                isBottomSheetVisible = true
            }
            true
        }

        marker2.setOnClickListener {
            if (isBottomSheetVisible) {
                val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
                bottomSheet1.startAnimation(slideDown)
                bottomSheet1.visibility = View.GONE
                isBottomSheetVisible = false
            } else {
                val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                bottomSheet1.startAnimation(slideUp)
                bottomSheet1.visibility = View.VISIBLE
                isBottomSheetVisible = true
            }
            true
        }
    }
}