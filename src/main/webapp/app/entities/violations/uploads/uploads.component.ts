import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ViolationsService } from '../service/violations.service';
import { Router } from '@angular/router';
@Component({
  selector: 'jhi-uploads',
  templateUrl: './uploads.component.html',
  standalone: true,
  imports: [FormsModule],
})
export class UploadsComponent {
  videoSrc: string | null = null;
  selectedFile: File | null = null;
  Whammy: any;
  @ViewChild('video') videoElement!: ElementRef<HTMLVideoElement>;
  @ViewChild('canvas') canvasElement!: ElementRef<HTMLCanvasElement>;

  private ctx!: CanvasRenderingContext2D;
  constructor(
    private violationsService: ViolationsService,
    private router: Router,
  ) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.videoSrc = URL.createObjectURL(this.selectedFile);
      input.value = '';
    }
  }

  onDetect(): void {
    if (this.selectedFile) {
      this.violationsService.detect(this.selectedFile).subscribe({
        next: (response: any) => {
          this.router.navigate(['/violations/detect'], { state: { data: response } });
        },
      });
    } else {
      console.error('Chưa có file được chọn.');
    }
  }
}
