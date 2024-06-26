package com.example.deckor_teamc_front

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.deckor_teamc_front.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class HomeFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var marker1: Marker
    private lateinit var marker2: Marker

    private var isImageOneDisplayed = true
    private var areMarkersVisible = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            navigateToSearchBuildingFragment()
        }

        if (!hasPermission()) {
            requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        val pinOnoffButton: ImageButton = binding.pinOnoffButton
        pinOnoffButton.setOnClickListener {
            // 마커를 토글하는 로직 추가
            if (areMarkersVisible) {
                hideMarkers()
            } else {
                showMarkers()
            }
            areMarkersVisible = !areMarkersVisible

            // 이미지 토글 로직은 그대로 유지
            if (isImageOneDisplayed) {
                pinOnoffButton.setImageResource(R.drawable.pin_on_button)
            } else {
                pinOnoffButton.setImageResource(R.drawable.pin_off_button)
            }
            isImageOneDisplayed = !isImageOneDisplayed
        }


    }

    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_view, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        val cameraUpdate = CameraUpdate.zoomTo(17.0)
        naverMap.moveCamera(cameraUpdate)

        val includedLayout = binding.includedLayout.root
        // sheet.xml 파일에서 TextView를 찾습니다.

        val standardBottomSheet = includedLayout.findViewById<FrameLayout>(R.id.standard_bottom_sheet)
        val standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        val buildingName = includedLayout.findViewById<TextView>(R.id.building_name)
        var selectedBuilding=0

        standardBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 바텀시트 상태가 변경될 때 호출됩니다.
                // 여기에 상태에 따른 작업을 추가할 수 있습니다.
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // 바텀시트가 접혀있을 때의 동작 설정
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // 바텀시트가 펼쳐져 있을 때의 동작 설정
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        selectedBuilding=0
                        // 바텀시트가 숨겨져 있을 때의 동작 설정
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 바텀시트가 슬라이드되는 동안 호출됩니다. (선택 사항)
            }
        })


        val innerMapButton = includedLayout.findViewById<Button>(R.id.modal_innermap_button)
        val layout1 = (binding.root).findViewById<ConstraintLayout>(R.id.fragment_home)

        // MainActivity의 onMapReady 코드를 여기에 추가
        marker1 = Marker().apply {
            position = LatLng(37.586868,127.0313414)
            map = naverMap
            icon = OverlayImage.fromResource(R.drawable.spot)
        }


        marker2 = Marker().apply {
            position = LatLng(37.5843837,127.0274333)
            map = naverMap
            icon = OverlayImage.fromResource(R.drawable.spot)
        }

        marker1.setOnClickListener {
            if(selectedBuilding!=1){
                buildingName.text=getString(R.string.building_name2)
                standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED}
            selectedBuilding=1
            true}

        marker2.setOnClickListener {
            if(selectedBuilding!=2){
                buildingName.text=getString(R.string.building_name)
                standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED}
            selectedBuilding=2
            true}


        innerMapButton.setOnClickListener {
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            val layoutInflater = LayoutInflater.from(requireContext())
            val layout2 = layoutInflater.inflate(R.layout.inner_maps, null)

            val layout3 = layout1.findViewById<FrameLayout>(R.id.map_view)
            val parent = layout3.parent as ViewGroup
            val index = parent.indexOfChild(layout3)
            parent.removeViewAt(index)
            parent.addView(layout2, index, layout3.layoutParams)
        }
    }

    private fun hideMarkers() {
        // 마커를 숨김
        marker1.map = null
        marker2.map = null
    }

    private fun showMarkers() {
        // 마커를 다시 보여줌
        marker1.map = naverMap
        marker2.map = naverMap
    }

    private fun navigateToSearchBuildingFragment() {
        val searchBuildingFragment = SearchBuildingFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, searchBuildingFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}