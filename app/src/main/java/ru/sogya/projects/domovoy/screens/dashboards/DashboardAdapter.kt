package ru.sogya.projects.domovoy.screens.dashboards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.slider.Slider
import ru.sogya.projects.domovoy.R
import ru.sogya.projects.domovoy.app.App
import ru.sogya.projects.domovoy.models.StatePresentation
import ru.sogya.projects.domovoy.utils.DiffUtilCallback

class DashboardAdapter(
    private val onStateClickListener: OnStateClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val differ: AsyncListDiffer<StatePresentation> =
        AsyncListDiffer(this, DiffUtilCallback())

    companion object {
        private const val IS_SENSOR = 0
        private const val IS_SUN = 1
        private const val IS_USER = 2
        private const val IS_SWITCH = 3
        private const val IS_MEDIA_PLAYER = 4
        private const val IS_CAMERA = 5
        private const val IS_COVER = 6

        private const val IS_OPEN = "open"
        private const val IS_CLOSED = "closed"
        private const val IS_UNAVAILABLE = "unavailable"
    }

    class SensorWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texViewLabel: TextView = itemView.findViewById(R.id.textFriendlyName)
        val textViewState: TextView = itemView.findViewById(R.id.textId)
        val iconView: ImageView = itemView.findViewById(R.id.imageViewIcon)
    }

    class SunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texViewLabel: TextView = itemView.findViewById(R.id.textFriendlyName)
        val textViewState: TextView = itemView.findViewById(R.id.textSunState)
        val iconView: ImageView = itemView.findViewById(R.id.imageViewIcon)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texViewLabel: TextView = itemView.findViewById(R.id.textViewUserLable)
        val iconView: ImageView = itemView.findViewById(R.id.imageViewUserIcon)
    }

    class SwitchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texViewLabel: TextView = itemView.findViewById(R.id.textViewSwitchLable)
        val textViewState: TextView = itemView.findViewById(R.id.textViewSwitchState)
        val switchState: SwitchCompat = itemView.findViewById(R.id.switchState)

    }

    class MediaPLayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewMediaPlayerName)
        val textViewId: TextView = itemView.findViewById(R.id.textViewEntityId)
        val buttonPowerOn: ImageButton = itemView.findViewById(R.id.imageButtonPowerPOn)
    }

    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textFriendlyName)
        val textViewId: TextView = itemView.findViewById(R.id.textId)
    }

    class CoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textCoverName)
        val coverSlider: Slider = itemView.findViewById(R.id.coverSlider)
        val buttonUp: AppCompatButton = itemView.findViewById(R.id.buttonCoverUp)
        val buttonDown: AppCompatButton = itemView.findViewById(R.id.buttonCoverDown)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textFriendlyName)
        val idTextView: TextView = itemView.findViewById(R.id.textId)
    }


    override fun getItemViewType(position: Int): Int {
        val entityId = differ.currentList[position].entityId
        return if (entityId.startsWith("sensor.") || entityId.startsWith("binary_sensor.")) {
            IS_SENSOR
        } else if (entityId.startsWith("sun.")) {
            IS_SUN
        } else if (entityId.startsWith("person."))
            IS_USER
        else if (entityId.startsWith("switch.")) {
            IS_SWITCH
        } else if (entityId.startsWith("media_player.")) {
            IS_MEDIA_PLAYER
        } else if (entityId.startsWith("camera.")) {
            IS_CAMERA
        } else if (entityId.startsWith("cover.")) {
            IS_COVER
        } else
            -1
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            IS_SENSOR -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_sensor_weather_item, parent, false)
                return SensorWeatherViewHolder(view)
            }

            IS_SUN -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_sun_item, parent, false)
                return SunViewHolder(view)
            }

            IS_USER -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_user_item, parent, false)
                return UserViewHolder(view)
            }

            IS_SWITCH -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_switch_item, parent, false)
                return SwitchViewHolder(view)
            }

            IS_MEDIA_PLAYER -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_media_player, parent, false)
                return MediaPLayerViewHolder(view)
            }

            IS_CAMERA -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.states_camera, parent, false)
                return CameraViewHolder(view)
            }

            IS_COVER -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_cover, parent, false)
                return CoverViewHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_default_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val statePresentation: StatePresentation = differ.currentList[position]
        val statePicture = buildString {
            append(statePresentation.ownerId)
            append(statePresentation.attributes?.entityPicture)
        }
        when (holder) {
            is SensorWeatherViewHolder -> {
                val stateIcon: Int
                if (statePresentation.attributes?.unitOfMeasurement != null) {
                    holder.textViewState.text = buildString {
                        append(statePresentation.state)
                        append(" ${statePresentation.attributes.unitOfMeasurement}")
                    }
                } else
                    holder.textViewState.text = statePresentation.state
                when (statePresentation.attributes?.deviceClass) {
                    "temperature" -> {
                        stateIcon = R.drawable.ic_thermometer
                    }

                    "humidity" -> {
                        stateIcon = R.drawable.ic_humidity
                    }

                    "data_size" -> {
                        stateIcon = R.drawable.ic_memory_48
                    }

                    "motion" -> {
                        stateIcon = R.drawable.ic_motion_sensor_48
                    }

                    else -> {
                        stateIcon = R.drawable.ic_sensor_48
                    }
                }
                holder.iconView.setImageResource(stateIcon)
                holder.texViewLabel.text = statePresentation.attributes?.friendlyName
            }

            is SunViewHolder -> {
                if (statePresentation.state == "above_horizon") {
                    holder.iconView.setImageResource(R.drawable.ic_sun)
                    holder.textViewState.text = "Над горизонтом"
                } else {
                    holder.iconView.setImageResource(R.drawable.ic_moon)
                    holder.textViewState.text = "За горизонтом"
                }
                holder.texViewLabel.text = statePresentation.attributes?.friendlyName
            }

            is UserViewHolder -> {
                holder.texViewLabel.text = statePresentation.attributes?.friendlyName
                Glide.with(App.getAppContext()).load(statePicture)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(holder.iconView)

            }

            is SwitchViewHolder -> {
                holder.texViewLabel.text = statePresentation.attributes?.friendlyName
                if (statePresentation.state == "on") {
                    holder.switchState.isChecked = true
                    holder.textViewState.text = "Включено"
                } else {
                    holder.switchState.isChecked = false
                    holder.textViewState.text = "Выключено"
                }
                holder.switchState.setOnCheckedChangeListener { _, isChecked ->
                    val state = if (isChecked) {
                        "turn_on"
                    } else
                        "turn_off"
                    onStateClickListener?.onSwitchStateChanged(statePresentation.entityId, state)
                }
            }

            is MediaPLayerViewHolder -> {
                holder.textViewId.text = statePresentation.attributes?.friendlyName
                holder.buttonPowerOn.setOnClickListener {
                    if (statePresentation.state == "off") {
                        onStateClickListener?.onClickWithCommand(
                            statePresentation.entityId,
                            "turn_on"
                        )
                    } else {
                        onStateClickListener?.onClickWithCommand(
                            statePresentation.entityId,
                            "turn_off"
                        )
                    }
                }
                holder.textView.text = statePresentation.state

            }

            is CameraViewHolder -> {
                holder.textViewName.text = statePresentation.attributes?.friendlyName
                holder.textViewId.text = statePresentation.entityId
            }

            is CoverViewHolder -> {
                holder.apply {
                    nameTextView.text = statePresentation.attributes?.friendlyName
                    coverSlider.value = statePresentation.attributes?.currentPosition?.toFloat()!!
                    coverSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                        override fun onStartTrackingTouch(slider: Slider) {
                            // Responds to when slider's touch event is being started
                        }

                        override fun onStopTrackingTouch(slider: Slider) {
                            onStateClickListener?.onSliderChangeValue(
                                statePresentation.entityId,
                                slider.value.toInt()
                            )
                        }
                    })
                    when (statePresentation.state) {
                        IS_OPEN -> {
                            buttonDown.isEnabled = true
                            buttonUp.isEnabled = false
                        }

                        IS_CLOSED -> {
                            buttonDown.isEnabled = false
                            buttonUp.isEnabled = true
                        }

                        IS_UNAVAILABLE -> {
                            buttonDown.isEnabled = false
                            buttonUp.isEnabled = false
                        }
                    }
                    buttonUp.setOnClickListener {
                        onStateClickListener?.onClickWithCommand(
                            statePresentation.entityId,
                            "open_cover"
                        )
                    }
                    buttonDown.setOnClickListener {
                        onStateClickListener?.onClickWithCommand(
                            statePresentation.entityId,
                            "close_cover"
                        )
                    }
                }
            }

            is ViewHolder -> {
                holder.nameTextView.text = statePresentation.attributes!!.friendlyName
                holder.idTextView.text = statePresentation.lastChanged
            }
        }
        holder.itemView.setOnClickListener {
            onStateClickListener?.onClick(statePresentation)
        }
        holder.itemView.setOnLongClickListener {
            onStateClickListener?.onLongClick(statePresentation)
            return@setOnLongClickListener true
        }
    }

    fun updateStatesList(statesArrayList: List<StatePresentation>) {
        differ.submitList(statesArrayList)
    }

    interface OnStateClickListener {
        fun onClick(statePresentation: StatePresentation)
        fun onLongClick(statePresentation: StatePresentation)
        fun onSwitchStateChanged(stateId: String, switchState: String) {}
        fun onClickWithCommand(stateId: String, command: String) {}
        fun onSliderChangeValue(stateId: String, value: Int) {}
    }
}
