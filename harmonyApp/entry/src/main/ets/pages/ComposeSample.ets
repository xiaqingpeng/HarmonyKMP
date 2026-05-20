import { registerComposeInteropBuilder } from 'compose/src/main/ets/compose/ArkUIView';
import {
  arkTsCallKotlinDemoBuilder,
  getAllPhotosBuilder,
  imageBuilder,
  selectSingleImageBuilder,
  textBuilder,
} from './ComposeInterops';

export function registerComposeInteropBuilders() {
  registerComposeInteropBuilder('arkImage', imageBuilder)
  registerComposeInteropBuilder('text', textBuilder)
  registerComposeInteropBuilder('selectSingleImage', selectSingleImageBuilder)
  registerComposeInteropBuilder('getAllPhotos', getAllPhotosBuilder)
  registerComposeInteropBuilder('arkTsCallKotlinDemo', arkTsCallKotlinDemoBuilder)
}
