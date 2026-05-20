import { fileUri, picker } from '@kit.CoreFileKit';
import { BusinessError } from '@kit.BasicServicesKit';
import abilityAccessCtrl from '@ohos.abilityAccessCtrl';
import common from '@ohos.app.ability.common';
import { arkTsCallKotlin, sendImagePathList, sendKotlinCallArkTsController } from 'libentry.so';
import { imageComponent } from '../Components/imageComponent';
import { ImagePickerPage } from './ComposeSelectPhotoPage';

class KotlinCallArkTsController {
  private toggleCount: number = 0;

  toggle(): void {
    this.toggleCount += 1;
    console.info(`[KotlinCallArkTsController] toggle count=${this.toggleCount}`);
  }
}

let kotlinCallArkTsController: KotlinCallArkTsController | undefined = undefined;

export function initKotlinCallArkTsController() {
  if (kotlinCallArkTsController) {
    return;
  }
  kotlinCallArkTsController = new KotlinCallArkTsController();

  // Delay one tick to avoid startup-time interop race with native runtime init.
  setTimeout(() => {
    if (!kotlinCallArkTsController) {
      return;
    }
    try {
      sendKotlinCallArkTsController(kotlinCallArkTsController);
      console.info('[KotlinCallArkTsController] controller sent to Kotlin');
    } catch (err) {
      const error: BusinessError = err as BusinessError;
      console.error(`[KotlinCallArkTsController] send failed: ${error.code}, ${error.message}`);
    }
  }, 0);
}

interface TextArgs {
  id: number;
  text: string;
  backgroundColor: string;
}

@Builder
export function imageBuilder(args?: object) {
  imageComponent();
}

@Builder
export function textBuilder(args: TextArgs) {
  TextCard();
}

@Builder
export function selectSingleImageBuilder(args?: object) {
  ImagePickerPage();
}

@Builder
export function getAllPhotosBuilder(args?: object) {
  GetAllPhotosCard();
}

@Builder
export function arkTsCallKotlinDemoBuilder(args?: object) {
  ArkTsCallKotlinDemoCard();
}

@Component
struct TextCard {
  @Consume compose_args: TextArgs;

  build() {
    Text(`${this.compose_args.id} ${this.compose_args.text}`)
      .backgroundColor(this.compose_args.backgroundColor)
      .width('100%')
      .height('100%')
      .borderRadius('5vp')
      .onClick(() => {
        console.info(`Text Clicked: ${JSON.stringify(this.compose_args)}`);
      });
  }
}

@Component
struct GetAllPhotosCard {
  @State imageUris: string[] = [];
  @State loading: boolean = false;
  @State errorMessage: string = '';

  private async requestAlbumPermission(context: common.Context): Promise<boolean> {
    try {
      const atManager = abilityAccessCtrl.createAtManager();
      const result = await atManager.requestPermissionsFromUser(context, [
        'ohos.permission.READ_MEDIA'
      ]);
      if (!result?.authResults?.length) {
        return false;
      }
      return result.authResults[0] === abilityAccessCtrl.GrantStatus.PERMISSION_GRANTED;
    } catch (err) {
      const error: BusinessError = err as BusinessError;
      console.error(`Request permission failed: ${error.code}, ${error.message}`);
      return false;
    }
  }

  private async loadAllImages(): Promise<void> {
    this.loading = true;
    this.errorMessage = '';

    try {
      const context = getContext(this) as common.Context;
      const granted = await this.requestAlbumPermission(context);
      if (!granted) {
        this.errorMessage = '未授权读取相册，请在系统设置中开启权限。';
        return;
      }

      const photoPicker: picker.PhotoViewPicker = new picker.PhotoViewPicker();
      const options: picker.PhotoSelectOptions = {
        MIMEType: picker.PhotoViewMIMETypes.IMAGE_TYPE,
        maxSelectNumber: 500
      };
      const result: picker.PhotoSelectResult = await photoPicker.select(options);
      const selectedUris = result?.photoUris ?? [];

      const imagePaths = selectedUris.map((item: string) => {
        try {
          const fileUriObject = new fileUri.FileUri(item);
          return fileUriObject.path;
        } catch (_) {
          // Fallback to original uri so one invalid item won't break the whole batch.
          return item;
        }
      });

      this.imageUris = imagePaths;
      // Pass a plain array copy to avoid @State proxy type mismatch in NAPI.
      sendImagePathList([...imagePaths]);
    } catch (err) {
      const error: BusinessError = err as BusinessError;
      console.error(`Load images failed: ${error.code}, ${error.message}`);
      this.errorMessage = '读取相册失败，请检查权限或媒体库状态。';
    } finally {
      this.loading = false;
    }
  }

  build() {
    Column({ space: 8 }) {
      Button('选择相册图片')
        .onClick(() => this.loadAllImages())
        .backgroundColor('#007DFF')
        .fontColor(Color.White)
        .margin({ top: 8 })

      if (this.loading) {
        Text('加载中...')
          .fontSize(12)
          .fontColor('#999999')
      }

      if (this.errorMessage) {
        Text(this.errorMessage)
          .fontSize(12)
          .fontColor('#D83B01')
      }

      Text(`已选 ${this.imageUris.length} 张`)
        .fontSize(12)
        .fontColor('#666666')
    }
  }
}

@Component
struct ArkTsCallKotlinDemoCard {
  @State resultText: string = '尚未调用 Kotlin 方法';

  build() {
    Column({ space: 8 }) {
      Text('ArkTS 调 Kotlin 字符串返回示例')
        .fontSize(16)
        .fontColor('#333333')

      Button('调用 Kotlin 方法')
        .onClick(() => {
          try {
            const ret: string = arkTsCallKotlin(123);
            this.resultText = ret;
          } catch (err) {
            const error: BusinessError = err as BusinessError;
            this.resultText = `调用失败: ${error.code}, ${error.message}`;
          }
        })
        .backgroundColor('#007DFF')
        .fontColor(Color.White)

      Text(this.resultText)
        .fontSize(14)
        .fontColor('#0A59F7')
    }
    .padding(16)
    .width('100%')
  }
}
