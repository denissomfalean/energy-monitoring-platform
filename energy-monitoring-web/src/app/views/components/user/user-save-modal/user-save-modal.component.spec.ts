import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSaveModalComponent } from './user-save-modal.component';

describe('UserSaveModalComponent', () => {
  let component: UserSaveModalComponent;
  let fixture: ComponentFixture<UserSaveModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSaveModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserSaveModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
