import { fileUri, picker } from '@kit.CoreFileKit';
import { BusinessError } from '@kit.BasicServicesKit';
import { sendBoxImagePath } from 'libentry.so';

@Component
export struct ImagePickerPage {
  @State imageUri: string = '';
  @State imagePath: string = '未选择图片';
  @State imageLocalPath: string = '';

  private toKotlinUri(src: string): string {
    const localPath = new fileUri.FileUri(src).path;
    return fileUri.getUriFromPath(localPath);
  }

  private async selectSingleImage() {
    try {
      const photoPicker: picker.PhotoViewPicker = new picker.PhotoViewPicker();
      const options: picker.PhotoSelectOptions = {
        MIMEType: picker.PhotoViewMIMETypes.IMAGE_TYPE,
        maxSelectNumber: 1
      };
      const result: picker.PhotoSelectResult = await photoPicker.select(options);
      const selectedUri = result?.photoUris?.[0];
      if (!selectedUri) {
        return;
      }

      const localPath = new fileUri.FileUri(selectedUri).path;
      const kotlinUri = this.toKotlinUri(selectedUri);
      this.imageUri = kotlinUri;
      // Send local path to Kotlin (same as CoilImageListTest chain).
      this.imagePath = localPath;
      // Keep URI only for ArkTS-side preview and diagnostics.
      this.imageLocalPath = kotlinUri;
    } catch (err) {
      const error: BusinessError = err as BusinessError;
      console.error(`Select image failed: ${error.code}, ${error.message}`);
      this.imagePath = `处理失败: ${error.message}`;
      this.imageLocalPath = '';
    }
  }

  build() {
    Column({ space: 10 }) {
      Row({ space: 15 }) {
        Button('选择图片')
          .onClick(() => {
            this.selectSingleImage();
          })
          .backgroundColor('#007DFF')
          .fontColor(Color.White)

        Button('发送给Kotlin')
          .backgroundColor('#FF6B35')
          .fontColor(Color.White)
          .enabled(this.imagePath !== '未选择图片' && this.imagePath.length > 0)
          .onClick(() => {
            sendBoxImagePath(this.imagePath);
          })
      }.width('100%').justifyContent(FlexAlign.SpaceEvenly)

      Column({ space: 5 }) {
        Text('发送给Kotlin的Path:')
          .fontSize(14)
          .fontWeight(FontWeight.Medium)
          .margin({ left: 20 })

        Text(this.imagePath)
          .fontSize(14)
          .textAlign(TextAlign.Start)
          .width('90%')
          .padding(10)
          .backgroundColor('#F5F5F5')
          .borderRadius(8)
          .maxLines(3)
          .textOverflow({ overflow: TextOverflow.Ellipsis })
      }

      if (this.imageLocalPath.length > 0) {
        Column({ space: 5 }) {
          Text('ArkTS预览URI:')
            .fontSize(14)
            .fontWeight(FontWeight.Medium)
            .margin({ left: 20 })

          Text(this.imageLocalPath)
            .fontSize(14)
            .textAlign(TextAlign.Start)
            .width('90%')
            .padding(10)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .maxLines(3)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
        }
      }

      Text('ArkTS侧图片预览:')
        .fontSize(16)
        .fontWeight(FontWeight.Medium)
        .margin({ left: 20, top: 10 })

      if (this.imageUri) {
        Image(this.imageUri)
          .width(220)
          .height(220)
          .objectFit(ImageFit.Cover)
          .borderRadius(12)
          .margin({ top: 8 })
      }
    }
    .width('100%')
    .padding(16)
  }
}
