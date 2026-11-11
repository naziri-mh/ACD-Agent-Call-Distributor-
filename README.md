# ACD - Agent Call Distributor

Java ile geliştirilmiş, katmanlı mimariye sahip bir müşteri destek merkezi simülasyonu. 
Sistem; müşteri mesajlarının kabulünü, temsilci durum yönetimini, FIFO tabanlı müsaitlik sırasını, öncelikli/normal kuyruk dağıtımını ve mesai bitişi transfer akışını modeller.

## Gereksinimler

- **Java 21** (veya üzeri)
- **Maven 3.6+**

## Mimari

- **Agent** — OFFLINE / AVAILABLE / BUSY durumları, FIFO müsaitlik sırası (`availableSince`)
- **Contact** — müşteriden gelen mesajı `Conversation`'a dönüştürür, `Session`'a iletir
- **Conversation** — bir sohbetin kimlik bilgisini taşır (id, contactId, customerInfo); mesaj geçmişini kendi içinde tutmaz
- **Session** — aktif sohbetlerin ve mesaj geçmişlerinin tek doğru kaynağı; sohbet başlatma/bitirme/transfer işlemlerini yönetir
- **QueueManager** — öncelikli ve normal kuyrukları ayrı ayrı yönetir (FIFO); öncelikli kuyruk her zaman önce dispatch edilir
- **AssignmentStrategy** — temsilci seçim stratejisini soyutlar (şu an: `FirstAvailableStrategy`)
- **SupportSystem** — tüm bileşenleri bir araya getiren orkestratör/facade
- **ScenarioStep / SimulationRunner** — senaryoyu adım adım tanımlayıp çalıştıran test/demo katmanı
- **Reporter** — simülasyon adımlarını ve sistem durumunu konsola raporlar

## Öne çıkan tasarım kararları

- Temsilci müsaitliği, offline'dan online olma dahil, katı bir FIFO sırasıyla yönetilir. (Bir temsilci online olduğunda veya işini bitirdiğinde müsaitlik sırasının en arkasına eklenir).
- Öncelikli kuyruk her zaman normal kuyruktan önce dispatch edilir.
- Bir temsilcinin mesaisi bittiğinde elindeki yarım kalan sohbet, `Session` üzerinden öncelikli kuyruğa transfer edilir.
- Mesaj geçmişi `Conversation` yerine `Session`'da tutulur; sohbet tamamlandığında hem aktif kayıt hem geçmiş `Session`'dan silinir

## Derleme ve Test

Projeyi derlemek için proje kök dizininde şu komutu çalıştırın:
```bash
mvn clean compile

