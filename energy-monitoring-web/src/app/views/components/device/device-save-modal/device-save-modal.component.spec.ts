import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceSaveModalComponent } from './device-save-modal.component';

describe('DeviceSaveModalComponent', () => {
  let component: DeviceSaveModalComponent;
  let fixture: ComponentFixture<DeviceSaveModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeviceSaveModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceSaveModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
