import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CompareService } from './compare.service';

interface CharStatus {
  char: string;
  result: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  maxAttempts = 6;
  wordLength = 5;

  attempts: CharStatus[][] = [];
  currentAttemptIndex = 0;
  complete = false;
  message = '';

  constructor(private compareService: CompareService) {}

  ngOnInit() {
    this.resetGrid();
  }

  resetGrid() {
    this.attempts = Array(this.maxAttempts)
      .fill(0)
      .map(() =>
        Array(this.wordLength)
          .fill(0)
          .map(() => ({ char: '', result: '' }))
      );
    this.currentAttemptIndex = 0;
    this.complete = false;
    this.message = '';
  }

  get canReset(): boolean {
    return this.complete || this.currentAttemptIndex >= this.maxAttempts;
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyDown(event: KeyboardEvent) {
    if (this.complete) return;

    const key = event.key;

    if (key === 'Enter') {
      this.submit();
    } else if (key === 'Backspace') {
      this.removeLastChar();
    } else if (/^[a-zA-Z]$/.test(key)) {
      this.addChar(key.toLowerCase());
    }
  }

  addChar(char: string) {
    const current = this.attempts[this.currentAttemptIndex];
    const filledCount = current.filter(c => c.char !== '').length;

    if (filledCount < this.wordLength) {
      current[filledCount].char = char;
    }
  }

  removeLastChar() {
    const current = this.attempts[this.currentAttemptIndex];
    for (let i = this.wordLength - 1; i >= 0; i--) {
      if (current[i].char !== '') {
        current[i].char = '';
        break;
      }
    }
  }

  submit() {
    const current = this.attempts[this.currentAttemptIndex];
    const input = current.map(c => c.char).join('');

    if (input.length !== this.wordLength) {
      this.message = `Please enter ${this.wordLength} letters.`;
      return;
    }

    this.compareService.compare(input).subscribe({
      next: (res) => {
        this.attempts[this.currentAttemptIndex] = res.status;
        this.currentAttemptIndex++;

        if (res.complete) {
          this.message = '🎉 You guessed it right!';
          this.complete = true;
        } else if (this.currentAttemptIndex >= this.maxAttempts) {
          this.message = 'Game over! The correct word was "madhu".';
          this.complete = true;
        } else {
          this.message = '';
        }
      },
      error: () => {
        this.message = 'Something went wrong.';
      }
    });
  }
}