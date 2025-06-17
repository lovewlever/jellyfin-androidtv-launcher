package org.jellyfin.androidtv.ui.gqcustom

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.jellyfin.androidtv.R
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSlideAppList(
	showTopSlideAppList: MutableState<Boolean>,
	appListVM: AppListViewModel = koinInject<AppListViewModel>(),
) {
	if (showTopSlideAppList.value) {
		var isActiveClose by remember { mutableStateOf(false) }
		val context = LocalContext.current
		val owner = LocalLifecycleOwner.current
		var firstIn by remember { mutableStateOf(true) }
		LaunchedEffect(true) {
			(context as ComponentActivity).onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
				override fun handleOnBackPressed() {
					if (showTopSlideAppList.value) {
						isActiveClose = true
					}
				}
			})
		}
		AnyPopDialog(
			isActiveClose = isActiveClose,
			onDismiss = { showTopSlideAppList.value = false },
			properties = AnyPopDialogProperties(direction = DirectionState.TOP)
		) {
			val context = LocalContext.current
			LaunchedEffect(true) {
				appListVM.queryAllApps()
			}
			Surface(
				color = Color(0xFF171717),
				tonalElevation = 8.dp,
				shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
				modifier = Modifier
					.fillMaxWidth()
					.height(220.dp)
			) {
				LazyHorizontalGrid(
					rows = GridCells.Fixed(2), modifier = Modifier
						.fillMaxSize()
						.padding(vertical = 8.dp)
						.focusable(true)
						.focusGroup()
				) {
					itemsIndexed(appListVM.appList) { index, item ->
						Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp).let {
							if (index == 0 || index == 1) it.padding(start = 8.dp)
							else if (index == (20 - 1) || index == (20 - 2)) it.padding(end = 8.dp) else it
						}) {
							val focusRequester = remember { FocusRequester() }
							var pressed by remember { mutableStateOf(false) }
							val scaleAnim by animateFloatAsState(targetValue = if (pressed) 1.12f else 1f, label = "")
							val shapeRoundedDp by animateDpAsState(targetValue = if (pressed) 24.dp else 16.dp, label = "")
							if (index == 0 && firstIn) {
								LaunchedEffect(Unit) {
									firstIn = false
									focusRequester.requestFocus()
								}
							}
							Card(
								onClick = {
									//focusIndex = index
									//focusRequester.requestFocus()
								},
								modifier = Modifier
									//.widthIn(min = 140.dp)
									.onFocusChanged { state ->
										pressed = state.isFocused
									}
									.onKeyEvent { keyEvent ->
										if (keyEvent.key == Key.Back || keyEvent.key == Key.Escape) {
											isActiveClose = true
											true
										}
										if ((keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter) && keyEvent.type == KeyEventType.KeyUp) {
											val intent = context.packageManager.getLeanbackLaunchIntentForPackage(item.pkgName)
											val intent2 = context.packageManager.getLaunchIntentForPackage(item.pkgName)

											if (intent != null) {
												context.startActivity(intent)
											} else if (intent2 != null) {
												context.startActivity(intent2)
											}
											isActiveClose = true
											//focusRequester.requestFocus()
											true
										} else {
											false
										}
									}
									.focusRequester(focusRequester)
									.focusable()
									.scale(scaleAnim)
								/*.focusable(true)
								.let {
									if (focusIndex == index) it.focusRequester(focusRequester) else it
								}
								.onFocusChanged { focusState ->
									Timber.d("\"FOCUS: \": ${index}; ${focusState.isFocused}")
								},*/
								/*elevation = CardDefaults.cardElevation(
									defaultElevation = 12.dp,
									focusedElevation = 24.dp,
									pressedElevation = 24.dp
								)*/,
								colors = CardDefaults.cardColors(containerColor = Color(0xFF424242)),
								shape = RoundedCornerShape(shapeRoundedDp)
							) {
								Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
									Spacer(modifier = Modifier.width(12.dp))
									item.icon?.toBitmap()?.asImageBitmap()?.let {
										Image(
											modifier = Modifier
												.size(50.dp),
											bitmap = it,
											contentDescription = ""
										)
									} ?: Image(
										modifier = Modifier
											.size(50.dp),
										painter = painterResource(id = R.drawable.ic_apps),
										contentDescription = ""
									)

									Spacer(modifier = Modifier.width(8.dp))

									Text(
										text = item.appName,
										fontSize = 16.sp,
										modifier = Modifier.width(100.dp),
										maxLines = 1,
										overflow = TextOverflow.Ellipsis,
										textAlign = TextAlign.Start,
										color = Color.White
									)
									Spacer(modifier = Modifier.width(12.dp))
								}
							}
						}
					}
				}

			}

		}
	}

}
